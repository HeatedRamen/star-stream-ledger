package com.pluralsight;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class AccountingApp {

    static Scanner input = new Scanner(System.in);
    static ArrayList<Transaction> transactionList = new ArrayList<>();

    public static void main(String[] args) {

        boolean isRunning = true, inLedger;
        String userChoice, ledgerChoice;

        // Read data from .csv and load into an ArrayList
        loadData();

        // Continuously runs program until user wants to exit
        do{
            // Displays home menu and prompt user for menu selection
            displayHomeMenu();
            userChoice = input.nextLine().toLowerCase();

            // Switch case for menu option and invalid input for default
            switch (userChoice){
                case "d":
                    addDeposit();
                    break;
                case "p":
                    makePayment();
                    break;
                case "l":
                    inLedger = true;

                    while(inLedger) {
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
                                chooseReports();
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
        }while(isRunning);
    }
    static void println(String message){
        System.out.println(message);
    }
    static void loadData(){

        try{

            // Opens and reads files
            FileReader fileReader = new FileReader("star_stream_transactions.csv");
            BufferedReader bufRead = new BufferedReader(fileReader);
            bufRead.readLine(); // Clears the header
            String line = bufRead.readLine();
            String[] parsedData;

            while(line != null){
                // Split the string and add it into transactions arraylist
                parsedData = line.split("\\|");
                transactionList.add(new Transaction(parsedData[0], parsedData[1], parsedData[2], parsedData[3],Double.parseDouble(parsedData[4])));
                line = bufRead.readLine();
            }
        }catch (Exception e){
            println("File could not be found");
        }
    }
    static void displayHomeMenu(){

        // Basic menu for now
        println("""
                ------------------------------------------------------------------------------------------
                
                                                        Menu Options
                                                   (D)  Make Deposit
                                                   (P)  Make Payment
                                                   (L)     Ledger
                                                   (X)      Exit
                
                ------------------------------------------------------------------------------------------""");
    }

    static void addDeposit(){

        // Add deposit into Arraylist
        Transaction userDeposit = promptDeposit();
        transactionList.add(userDeposit);

        // Write into the file
        logTransaction(userDeposit);
    }
    static Transaction promptDeposit(){

        // Format for date / time
        DateTimeFormatter logDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter logTimeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

        // Fetch current date time and prompt user for deposit details
        ZonedDateTime depositTime = LocalDateTime.now().atZone(ZoneId.systemDefault());

        println("What is the reason for the deposit?");
        String description = input.nextLine().trim();
        println("What is your constellation modifier?");
        String vendor = input.nextLine().trim();
        println("How many coins would like to deposit?");
        double amount = input.nextDouble();
        input.nextLine(); // Clears buffer

        Transaction userDeposit = new Transaction(depositTime.format(logDateFormat), depositTime.format(logTimeFormat), description, vendor, amount);
        return userDeposit;
    }
    static void makePayment(){

        // Add Payment into Arraylist
        Transaction userPayment = promptPayment();
        transactionList.add(userPayment);

        // Write into the file
        logTransaction(userPayment);
    }
    static Transaction promptPayment(){

        // Format for date / time
        DateTimeFormatter logDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter logTimeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

        // Fetch current date time and prompt user for deposit details
        ZonedDateTime depositTime = LocalDateTime.now().atZone(ZoneId.systemDefault());

        println("What is the reason for the payment?");
        String description = input.nextLine().trim();
        println("Which Dokkaebi is receiving your payment?");
        String vendor = input.nextLine().trim();
        println("How many coins would like to pay?");
        double amount = input.nextDouble();
        input.nextLine(); // Clears buffer

        Transaction userPayment = new Transaction(depositTime.format(logDateFormat), depositTime.format(logTimeFormat), description, vendor, -amount);
        return userPayment;
    }
    static void logTransaction(Transaction userDeposit){

        try{
            FileWriter fileWriter = new FileWriter("star_stream_transactions.csv", true);
            BufferedWriter bufWrite = new BufferedWriter(fileWriter);

            bufWrite.write(userDeposit.getDate() +"|" +userDeposit.getTime() + "|" + userDeposit.getDescription()
                            + "|" + userDeposit.getVendor() +"|" + userDeposit.getAmount() +"\n");
            bufWrite.close();

        } catch (Exception e) {
            println("Could not write. File not found");
        }
    }

    static void displayLedgerMenu(){
        println("""
                ------------------------------------------------------------------------------------------
                
                                                        Menu Options
                                                   (A) Show All Transactions
                                                   (D)    Show Deposits
                                                   (P)    Show Payments
                                                   (R)  Generate Reports
                                                   (H)    Back to Home
                
                ------------------------------------------------------------------------------------------""");
    }
    static void showAllTransaction(){
        System.out.printf("%-10s | %-10s | %-35s | %-35s | %s", "Date",
                "Time", "Description", "Vendor", "Amount\n");
        for (Transaction data: transactionList) {
            System.out.printf("%-10s | %-10s | %-35s | %-35s | %.2f\n", data.getDate(),
                    data.getTime(), data.getDescription(), data.getVendor(), data.getAmount());
        }
    }
    static void showDeposits(){

        System.out.printf("%-10s | %-10s | %-35s | %-35s | %s", "Date",
                "Time", "Description", "Vendor", "Amount\n");
        for (Transaction data : transactionList){
            if (data.getAmount() > 0){
                System.out.printf("%-10s | %-10s | %-35s | %-35s | %.2f\n", data.getDate(),
                        data.getTime(), data.getDescription(), data.getVendor(), data.getAmount());
            }
        }
    }
    static void showPayments(){
        System.out.printf("%-10s | %-10s | %-35s | %-35s | %s", "Date",
                "Time", "Description", "Vendor", "Amount\n");
        for (Transaction data : transactionList){
            if (data.getAmount() < 0){
                System.out.printf("%-10s | %-10s | %-35s | %-35s | %.2f\n", data.getDate(),
                        data.getTime(), data.getDescription(), data.getVendor(), data.getAmount());
            }
        }
    }
    static void chooseReports(){}
}
