package com.pluralsight;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class AccountingApp {

    static Scanner input = new Scanner(System.in);
    static ArrayList<Transaction> transactionList = new ArrayList<>();

    static DateTimeFormatter logDateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    static DateTimeFormatter logDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static DateTimeFormatter logTimeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void main(String[] args) {

        // Initialize variables for menu loops and choices
        boolean isRunning = true, inLedger, inReport;
        String userChoice, ledgerChoice, reportChoice;

        // Read data from .csv and load into an ArrayList
        loadData();

        // Continuously runs program until user wants to exit
        do {
            // Displays home menu and prompt user for menu selection
            displayHomeMenu();
            userChoice = input.nextLine().toLowerCase().trim();

            // Switch case for menu option and invalid input for default
            switch (userChoice) {
                case "d":
                    addDeposit();
                    break;
                case "p":
                    makePayment();
                    break;
                case "l":
                    // Sorting Arraylist in descending / by newest before any displays
                    sortTransactions();
                    inLedger = true; // Always start as true beforehand in case the user opens ledger multiple times

                    // Displays Ledger menu and prompt user for ledger selection and continuous loop until user command exits
                    while (inLedger) {
                        displayLedgerMenu();
                        ledgerChoice = input.nextLine().toLowerCase().trim();

                        // Switch case for the ledger options and invalid input for default
                        switch (ledgerChoice) {
                            case "a":
                                showAllTransaction();
                                break;
                            case "d":
                                showDeposits();
                                break;
                            case "p":
                                showPayments();
                                break;
                            case "r":
                                inReport = true; // Always start as true beforehand in case the user opens reports multiple times

                                // Displays Ledger menu and prompt user for ledger selection and continuous loop until user command exits
                                while (inReport) {
                                    displayReportMenu();
                                    reportChoice = input.nextLine().trim();

                                    // Switch case for report options and invalid input for default
                                    switch (reportChoice) {
                                        case "1":
                                            displayMonthToDate();
                                            break;
                                        case "2":
                                            displayPreviousMonth();
                                            break;
                                        case "3":
                                            displayYearToDate();
                                            break;
                                        case "4":
                                            displayPreviousYear();
                                            break;
                                        case "5":
                                            promptVendorSearch();
                                            break;
                                        case "6":
                                            promptCustomSearch();
                                            break;
                                        case "0":
                                            inReport = false;
                                            break;
                                        default:
                                            println("Invalid Option... TRY AGAIN >:(");
                                    }
                                }
                                break;
                            case "h":
                                inLedger = false;
                                break;
                            default:
                                println("Invalid Option... TRY AGAIN >:(");

                        }
                    }
                    break;
                case "x":
                    isRunning = false;
                    break;
                default:
                    println("Invalid Option... TRY AGAIN >:(");
            }
        } while (isRunning);
    }

    static void loadData() {

        try {
            // Opens and reads files
            FileReader fileReader = new FileReader("star_stream_transactions.csv");
            BufferedReader bufRead = new BufferedReader(fileReader);
            bufRead.readLine(); // Clears the header
            String line = bufRead.readLine();
            String[] parsedData;

            while (line != null) {
                // Split the string and add it into transactions arraylist
                parsedData = line.split("\\|");

                // Convert String into LocalDateTime
                LocalDateTime dateTime = LocalDateTime.parse(parsedData[0] + " " + parsedData[1], logDateTimeFormat);

                // Add Transaction into Array list
                transactionList.add(new Transaction(dateTime, parsedData[2], parsedData[3], Long.parseLong(parsedData[4])));
                line = bufRead.readLine(); // Next line
            }
        }
        // Catch case in case file is not found
        catch (Exception e) {
            println("File could not be found");
        }
    }

    static void displayHomeMenu() {
        // Basic menu for now
        println("""
                +------------------------------------------------------------------------------------------+
                |                                                                                          |
                |                                        Menu Options                              (•ᴗ•)   |
                |                                   (D) Make Deposit                               /   \\   |
                |                                   (P) Make Payment                                \\_/    |
                |                                   (L) Ledger                                             |
                |                                   (X) Exit                                               |
                |                                                                                          |
                +------------------------------------------------------------------------------------------+""");
    }

    static void displayLedgerMenu() {

        // Basic print menu for now
        println("""
                +------------------------------------------------------------------------------------------+
                |                                                                                          |
                |                                        Menu Options                              (•ᴗ•)   |
                |                                   (A) Show All Transactions                      /   \\   |
                |                                   (D) Show Deposits                               \\_/    |
                |                                   (P) Show Payments                                      |
                |                                   (R) Generate Reports                                   |
                |                                   (H) Back to Home                                       |
                |                                                                                          |
                +------------------------------------------------------------------------------------------+""");
    }

    static void displayReportMenu() {

        // Basic print menu for now
        println("""
                +------------------------------------------------------------------------------------------+
                |                                                                                          |
                |                                      Report Options                              (•ᴗ•)   |
                |                                   (1) Month - to - Date                          /   \\   |
                |                                   (2) Previous Month                              \\_/    |
                |                                   (3) Year - to - Date                                   |
                |                                   (4) Previous Year                                      |
                |                                   (5) Search by Vendor                                   |
                |                                   (6) Custom                                             |
                |                                   (0) Exit                                               |
                |                                                                                          |
                +------------------------------------------------------------------------------------------+""");
    }

    static void addDeposit() {

        // Add deposit into Arraylist
        Transaction userDeposit = promptDeposit();
        transactionList.add(userDeposit);

        // Write into the file
        logTransaction(userDeposit);
    }

    static Transaction promptDeposit() {

        // Fetch current date time
        ZonedDateTime depositTime = LocalDateTime.now().atZone(ZoneId.systemDefault());

        // Prompt user for information
        println("What is the reason for the deposit?");
        String description = input.nextLine().trim();
        println("What is your constellation modifier?");
        String vendor = input.nextLine().trim();
        println("How many coins would like to deposit?");
        long amount = input.nextLong();
        input.nextLine(); // Clears buffer

        // Return deposit information to be added into ArrayList and written into file
         return new Transaction(depositTime.toLocalDateTime(), description, vendor, amount);
    }

    static void makePayment() {

        // Add Payment into Arraylist
        Transaction userPayment = promptPayment();
        transactionList.add(userPayment);

        // Write into the file
        logTransaction(userPayment);
    }

    static Transaction promptPayment() {

        // Fetch current date time and prompt user for deposit details
        ZonedDateTime depositTime = LocalDateTime.now().atZone(ZoneId.systemDefault());

        // Prompt user for information
        println("What is the reason for the payment?");
        String description = input.nextLine().trim();
        println("Which Dokkaebi is receiving your payment?");
        String vendor = input.nextLine().trim();
        println("How many coins would like to pay?");
        long amount = input.nextLong();
        input.nextLine(); // Clears buffer

        // Returns payment information to be added into ArrayList and written into file
        return new Transaction(depositTime.toLocalDateTime(), description, vendor, -amount);
    }

    static void logTransaction(Transaction userDeposit) {

        try {
            // Opening Buffered Writer and making sure to append
            FileWriter fileWriter = new FileWriter("star_stream_transactions.csv", true);
            BufferedWriter bufWrite = new BufferedWriter(fileWriter);

            // Writing to match the formatting of the .csv file
            bufWrite.write(userDeposit.getDateTime().format(logDateFormat) + "|" + userDeposit.getDateTime().format(logTimeFormat) + "|" +
                    userDeposit.getDescription() + "|" + userDeposit.getVendor() + "|" + userDeposit.getAmount() + "\n");
            bufWrite.close(); // Close write

        }
        // Catch case in case file could not be found
        catch (Exception e) {
            println("Could not write. File not found");
        }
    }


    static void printHeader(){

        // Prints formatted header
        System.out.printf("%-10s | %-8s | %-35s | %-35s | %s", "Date",
                "Time", "Description", "Vendor", "Amount\n");
    }

    static void showAllTransaction() {

        // Prints header and goes through ArrayList and prints information into a table like manner
        printHeader();
        for (Transaction data : transactionList) {
            System.out.printf("%-10s | %-8s | %-35s | %-35s | %d\n", data.getDateTime().format(logDateFormat),
                    data.getDateTime().format(logTimeFormat), data.getDescription(), data.getVendor(), data.getAmount());
        }
    }

    static void showDeposits() {

        // Prints header and goes through ArrayList and prints out only deposits (Only positive amounts) into a table like manner
        printHeader();
        for (Transaction data : transactionList) {
            if (data.getAmount() > 0) {
                System.out.printf("%-10s | %-8s | %-35s | %-35s | %d\n", data.getDateTime().format(logDateFormat),
                        data.getDateTime().format(logTimeFormat), data.getDescription(), data.getVendor(), data.getAmount());
            }
        }
    }

    static void showPayments() {

        // Prints header and goes through ArrayList and prints out only payments (Only negative amounts) into a table like manner
        printHeader();
        for (Transaction data : transactionList) {
            if (data.getAmount() < 0) {
                System.out.printf("%-10s | %-8s | %-35s | %-35s | %d\n", data.getDateTime().format(logDateFormat),
                        data.getDateTime().format(logTimeFormat), data.getDescription(), data.getVendor(), data.getAmount());
            }
        }
    }

    static void sortTransactions() {

        // Sort transaction list gets date and time, B goes first in order to get descending sort
        transactionList.sort((a, b) -> b.getDateTime().compareTo(a.getDateTime()));
    }

    static void displayMonthToDate() {

        // Get current date time
        LocalDateTime timeNow = LocalDateTime.now();


        for (Transaction data : transactionList) {
            // Make comparison if transaction data's month is equal to current month and year then print out matches
            if (timeNow.getMonth() == data.getDateTime().getMonth() && timeNow.getYear() == data.getDateTime().getYear()) {
                System.out.printf("%-10s | %-8s | %-35s | %-35s | %d\n", data.getDateTime().format(logDateFormat),
                        data.getDateTime().format(logTimeFormat), data.getDescription(), data.getVendor(), data.getAmount());
            }
        }
    }

    static void displayPreviousMonth() {

        // Get current date time but go back one month
        LocalDateTime timeNow = LocalDateTime.now().minusMonths(1);

        for (Transaction data : transactionList) {
            // Make comparison if transaction data's month is equal to the previous month and current year then print out matches
            if (timeNow.getMonth() == data.getDateTime().getMonth() && timeNow.getYear() == data.getDateTime().getYear()) {
                System.out.printf("%-10s | %-8s | %-35s | %-35s | %d\n", data.getDateTime().format(logDateFormat),
                        data.getDateTime().format(logTimeFormat), data.getDescription(), data.getVendor(), data.getAmount());
            }
        }
    }

    static void displayYearToDate() {

        // Get current date time
        LocalDateTime timeNow = LocalDateTime.now();

        for (Transaction data : transactionList) {
            // Make comparison if transaction data's year is equal to current year then print out matches
            if (timeNow.getYear() == data.getDateTime().getYear()) {
                System.out.printf("%-10s | %-8s | %-35s | %-35s | %d\n", data.getDateTime().format(logDateFormat),
                        data.getDateTime().format(logTimeFormat), data.getDescription(), data.getVendor(), data.getAmount());
            }
        }
    }

    static void displayPreviousYear() {

        // Get current date time but go back one year
        LocalDateTime timeNow = LocalDateTime.now().minusYears(1);

        for (Transaction data : transactionList) {
            // Make comparison if transaction data's year is equal to previous year then print out matches
            if (timeNow.getYear() == data.getDateTime().getYear()) {
                System.out.printf("%-10s | %-8s | %-35s | %-35s | %d\n", data.getDateTime().format(logDateFormat),
                        data.getDateTime().format(logTimeFormat), data.getDescription(), data.getVendor(), data.getAmount());
            }
        }
    }

    static void promptVendorSearch() {

        // Prompt user for vendor
        println("What is the name of the vendor you're looking for");
        String userVendor = input.nextLine().trim();

        for (Transaction data : transactionList) {
            // Make comparison if transaction data's vendor is equal to user supplied vendor and print out matches
            if (userVendor.equalsIgnoreCase(data.getVendor())) {
                System.out.printf("%-10s | %-8s | %-35s | %-35s | %d\n", data.getDateTime().format(logDateFormat),
                        data.getDateTime().format(logTimeFormat), data.getDescription(), data.getVendor(), data.getAmount());
            }
        }
    }

    static void promptCustomSearch() {

        // Basic looking custom search menu
        println("""
                +~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~CUSTOM  SEARCH~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~+
                |                                    Press return to skip                                  |
                +~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~+""");
        // Prompt user for search filters
        println("What is the start date for your search? (yyyy-MM-dd)");
        String userStartDate = input.nextLine().trim();
        println("What is the end date for your search? (yyyy-MM-dd)");
        String userEndDate = input.nextLine().trim();
        println("What is the description for your transaction?");
        String userDescription = input.nextLine().trim();
        println("What is the vendor for your transaction?");
        String userVendor = input.nextLine().trim();
        println("What is the minimum range for your transaction?");
        String userMinAmount = input.nextLine().trim();
        println("What is the maximum range for your transaction?");
        String userMaxAmount = input.nextLine().trim();

        // To convert String into their respective variable
        Integer minAmount = null, maxAmount = null;
        LocalDateTime startDate = null, endDate = null;

        // Try catch statement in case the user doesn't enter in valid input :P
        try {
            // Always checks if the string isn't empty, if it is empty leave it as null
            if (!userStartDate.isEmpty()) {
                startDate = LocalDate.parse(userStartDate, logDateFormat).atStartOfDay();
            }
            if (!userEndDate.isEmpty()) {
                endDate = LocalDate.parse(userEndDate, logDateFormat).atTime(23, 59,59);
            }
            if (!userMinAmount.isEmpty()) {
                minAmount = Integer.parseInt(userMinAmount);
            }
            if (!userMaxAmount.isEmpty()) {
                maxAmount = Integer.parseInt(userMaxAmount);
            }
        } catch (Exception e){
            println("Invalid input");
            return;
        }

        for (Transaction data : transactionList) {

            // One HUGE if statement that checks if the user input is empty / null OR equal to the transaction data
            // used !Before Start Date and !After End Date to be inclusive for the input dates
            if ((startDate == null || (!data.getDateTime().isBefore(startDate))) &&
                    (endDate == null || (!data.getDateTime().isAfter(endDate))) &&
                    (userDescription.isEmpty() || (userDescription.equalsIgnoreCase(data.getDescription()))) &&
                    (userVendor.isEmpty() || (userVendor.equalsIgnoreCase(data.getVendor()))) &&
                    (minAmount == null || data.getAmount() >= minAmount) &&
                    (maxAmount == null || data.getAmount() <= maxAmount)) {

                System.out.printf("%-10s | %-8s | %-35s | %-35s | %d\n", data.getDateTime().format(logDateFormat),
                        data.getDateTime().format(logTimeFormat), data.getDescription(), data.getVendor(), data.getAmount());
            }
        }
    }

    static void println(String message) {
        System.out.println(message);
    }
}
