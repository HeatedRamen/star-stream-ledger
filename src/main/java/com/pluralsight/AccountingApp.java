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

    static DateTimeFormatter logDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static DateTimeFormatter logTimeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void main(String[] args) {

        boolean isRunning = true, inLedger, inReport;
        String userChoice, ledgerChoice, reportChoice;

        // Read data from .csv and load into an ArrayList
        loadData();

        // Continuously runs program until user wants to exit
        do {
            // Displays home menu and prompt user for menu selection
            displayHomeMenu();
            userChoice = input.nextLine().toLowerCase();

            // Switch case for menu option and invalid input for default
            switch (userChoice) {
                case "d":
                    addDeposit();
                    break;
                case "p":
                    makePayment();
                    break;
                case "l":

                    // Sorting Arraylist in descending / by newest
                    sortTransactions();
                    inLedger = true;

                    while (inLedger) {
                        displayLedgerMenu();
                        ledgerChoice = input.nextLine().toLowerCase();

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
                                inReport = true;

                                while (inReport) {
                                    displayReportMenu();
                                    reportChoice = input.nextLine();

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

        // Format so String to DateTime can read
        DateTimeFormatter logDateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
                LocalDateTime dateTime = LocalDateTime.parse(parsedData[0] + " " + parsedData[1], logDateTimeFormat);
                transactionList.add(new Transaction(dateTime, parsedData[2], parsedData[3], Long.parseLong(parsedData[4])));
                line = bufRead.readLine();
            }
        } catch (Exception e) {
            println("File could not be found");
        }
    }

    static void displayHomeMenu() {

        // Basic menu for now
        println("""
                ------------------------------------------------------------------------------------------
                
                                                     Menu Options
                                                   (D) Make Deposit
                                                   (P) Make Payment
                                                   (L) Ledger
                                                   (X) Exit
                
                ------------------------------------------------------------------------------------------""");
    }

    static void addDeposit() {

        // Add deposit into Arraylist
        Transaction userDeposit = promptDeposit();
        transactionList.add(userDeposit);

        // Write into the file
        logTransaction(userDeposit);
    }

    static Transaction promptDeposit() {

        // Fetch current date time and prompt user for deposit details
        ZonedDateTime depositTime = LocalDateTime.now().atZone(ZoneId.systemDefault());

        println("What is the reason for the deposit?");
        String description = input.nextLine().trim();
        println("What is your constellation modifier?");
        String vendor = input.nextLine().trim();
        println("How many coins would like to deposit?");
        long amount = input.nextLong();
        input.nextLine(); // Clears buffer

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

        println("What is the reason for the payment?");
        String description = input.nextLine().trim();
        println("Which Dokkaebi is receiving your payment?");
        String vendor = input.nextLine().trim();
        println("How many coins would like to pay?");
        long amount = input.nextLong();
        input.nextLine(); // Clears buffer

        return new Transaction(depositTime.toLocalDateTime(), description, vendor, -amount);

    }

    static void logTransaction(Transaction userDeposit) {

        try {
            FileWriter fileWriter = new FileWriter("star_stream_transactions.csv", true);
            BufferedWriter bufWrite = new BufferedWriter(fileWriter);

            bufWrite.write(userDeposit.getDateTime().format(logDateFormat) + "|" + userDeposit.getDateTime().format(logTimeFormat) + "|" +
                    userDeposit.getDescription() + "|" + userDeposit.getVendor() + "|" + userDeposit.getAmount() + "\n");
            bufWrite.close();

        } catch (Exception e) {
            println("Could not write. File not found");
        }
    }

    static void displayLedgerMenu() {
        println("""
                ------------------------------------------------------------------------------------------
                
                                                        Menu Options
                                                   (A) Show All Transactions
                                                   (D) Show Deposits
                                                   (P) Show Payments
                                                   (R) Generate Reports
                                                   (H) Back to Home
                
                ------------------------------------------------------------------------------------------""");
    }

    static void showAllTransaction() {

        System.out.printf("%-10s | %-10s | %-35s | %-35s | %s", "Date",
                "Time", "Description", "Vendor", "Amount\n");
        for (Transaction data : transactionList) {
            System.out.printf("%-10s | %-10s | %-35s | %-35s | %d\n", data.getDateTime().format(logDateFormat),
                    data.getDateTime().format(logTimeFormat), data.getDescription(), data.getVendor(), data.getAmount());
        }
    }

    static void showDeposits() {

        System.out.printf("%-10s | %-10s | %-35s | %-35s | %s", "Date",
                "Time", "Description", "Vendor", "Amount\n");
        for (Transaction data : transactionList) {
            if (data.getAmount() > 0) {
                System.out.printf("%-10s | %-10s | %-35s | %-35s | %d\n", data.getDateTime().format(logDateFormat),
                        data.getDateTime().format(logTimeFormat), data.getDescription(), data.getVendor(), data.getAmount());
            }
        }
    }

    static void showPayments() {

        System.out.printf("%-10s | %-10s | %-35s | %-35s | %s", "Date",
                "Time", "Description", "Vendor", "Amount\n");
        for (Transaction data : transactionList) {
            if (data.getAmount() < 0) {
                System.out.printf("%-10s | %-10s | %-35s | %-35s | %d\n", data.getDateTime().format(logDateFormat),
                        data.getDateTime().format(logTimeFormat), data.getDescription(), data.getVendor(), data.getAmount());
            }
        }
    }

    static void displayReportMenu() {
        println("""
                ------------------------------------------------------------------------------------------
                
                                                       Report Options
                                                   (1) Month - to - Date
                                                   (2) Previous Month
                                                   (3) Year - to - Date
                                                   (4) Previous Year
                                                   (5) Search by Vendor
                                                   (6) Custom
                                                   (0) Exit
                
                ------------------------------------------------------------------------------------------""");
    }

    static void sortTransactions() {
        transactionList.sort((a, b) -> b.getDateTime().compareTo(a.getDateTime()));
    }

    static void displayMonthToDate() {

        LocalDateTime timeNow = LocalDateTime.now();

        for (Transaction data : transactionList) {
            if (timeNow.getMonth() == data.getDateTime().getMonth() && timeNow.getYear() == data.getDateTime().getYear()) {
                System.out.printf("%-10s | %-10s | %-35s | %-35s | %d\n", data.getDateTime().format(logDateFormat),
                        data.getDateTime().format(logTimeFormat), data.getDescription(), data.getVendor(), data.getAmount());
            }

        }
    }

    static void displayPreviousMonth() {

        LocalDateTime timeNow = LocalDateTime.now().minusMonths(1);

        for (Transaction data : transactionList) {
            if (timeNow.getMonth() == data.getDateTime().getMonth() && timeNow.getYear() == data.getDateTime().getYear()) {
                System.out.printf("%-10s | %-10s | %-35s | %-35s | %d\n", data.getDateTime().format(logDateFormat),
                        data.getDateTime().format(logTimeFormat), data.getDescription(), data.getVendor(), data.getAmount());
            }
        }
    }

    static void displayYearToDate() {

        LocalDateTime timeNow = LocalDateTime.now();

        for (Transaction data : transactionList) {
            if (timeNow.getYear() == data.getDateTime().getYear()) {
                System.out.printf("%-10s | %-10s | %-35s | %-35s | %d\n", data.getDateTime().format(logDateFormat),
                        data.getDateTime().format(logTimeFormat), data.getDescription(), data.getVendor(), data.getAmount());
            }
        }
    }

    static void displayPreviousYear() {

        LocalDateTime timeNow = LocalDateTime.now().minusYears(1);

        for (Transaction data : transactionList) {
            if (timeNow.getYear() == data.getDateTime().getYear()) {
                System.out.printf("%-10s | %-10s | %-35s | %-35s | %d\n", data.getDateTime().format(logDateFormat),
                        data.getDateTime().format(logTimeFormat), data.getDescription(), data.getVendor(), data.getAmount());
            }
        }
    }

    static void promptVendorSearch() {
        //
        println("What is the name of the vendor you're looking for");
        String userVendor = input.nextLine();

        for (Transaction data : transactionList) {
            if (userVendor.equalsIgnoreCase(data.getVendor())) {
                System.out.printf("%-10s | %-10s | %-35s | %-35s | %d\n", data.getDateTime().format(logDateFormat),
                        data.getDateTime().format(logTimeFormat), data.getDescription(), data.getVendor(), data.getAmount());
            }
        }
    }

    static void promptCustomSearch() {
        println("""
                        ~~~~~~~~~~~~~~~CUSTOM SEARCH~~~~~~~~~~~~~~~
                                   Press return to skip
                What is the start date for your search? (yyyy-MM-dd)""");
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

        // Converting String into LocalDateTime for Comparison
        LocalDateTime startDate = null, endDate = null;
        if (!userStartDate.isEmpty()) {
            startDate = LocalDateTime.parse(userStartDate, logDateFormat);
        }
        if (!userEndDate.isEmpty()) {
            endDate = LocalDateTime.parse(userEndDate, logDateFormat);
        }

        Integer minAmount = null, maxAmount = null;
        if (!userMinAmount.isEmpty()) {
            minAmount = Integer.parseInt(userMinAmount);
        }
        if (!userMaxAmount.isEmpty()) {
            maxAmount = Integer.parseInt(userMaxAmount);
        }

        for (Transaction data : transactionList) {

            // One HUGE if statement that checks if the user input is empty OR equal to the transaction data
            if ((startDate == null || (!data.getDateTime().isAfter(startDate))) &&
                    (endDate == null || (!data.getDateTime().isBefore(endDate))) &&
                    (userDescription.isEmpty() || (userDescription.equalsIgnoreCase(data.getDescription()))) &&
                    (userVendor.isEmpty() || (userVendor.equalsIgnoreCase(data.getVendor()))) &&
                    (minAmount == null || data.getAmount() >= minAmount) &&
                    (maxAmount == null || data.getAmount() <= maxAmount)) {

                System.out.printf("%-10s | %-10s | %-35s | %-35s | %d\n", data.getDateTime().format(logDateFormat),
                        data.getDateTime().format(logTimeFormat), data.getDescription(), data.getVendor(), data.getAmount());
            }
        }

    }

    static void println(String message) {
        System.out.println(message);
    }
}
