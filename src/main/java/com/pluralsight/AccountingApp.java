package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;

public class AccountingApp {

    static Scanner input = new Scanner(System.in);
    static ArrayList<Transaction> transactionList = new ArrayList<>();

    public static void main(String[] args) {

        boolean isRunning = true, inLedger;
        String userChoice, ledgerChoice;

        loadData();

        do{
            userChoice = input.nextLine().toLowerCase();

            displayHomeMenu();

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
            FileReader fileReader = new FileReader("transactions.csv");
            BufferedReader bufRead = new BufferedReader(fileReader);
            bufRead.readLine(); // Clears the header
            String line = bufRead.readLine();
            String[] parsedData;

            while(line != null){
                parsedData = line.split("\\|");
                transactionList.add(new Transaction(parsedData[0], parsedData[1], parsedData[2], parsedData[3],Double.parseDouble(parsedData[4])));
                line = bufRead.readLine();
            }
        }catch (Exception e){
            println("File could not be found");
        }
    }
    static void displayHomeMenu(){}
    static void addDeposit(){}
    static void makePayment(){}
    static void displayLedgerMenu(){}
    static void showAllTransaction(){
        System.out.printf("%-10s | %-8s | %-20s | %-20s | %s", "Date",
                "Time", "Description", "Vendor", "Amount\n");
        for (Transaction data: transactionList)
        System.out.printf("%-10s | %-8s | %-20s | %-20s | %.2f\n", data.getDate(),
                data.getTime(), data.getDescription(), data.getVendor(), data.getAmount());
    }
    static void showDeposits(){}
    static void showPayments(){}
    static void chooseReports(){}
}
