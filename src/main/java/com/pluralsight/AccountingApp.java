package com.pluralsight;

import java.util.ArrayList;
import java.util.Scanner;

public class AccountingApp {

    static Scanner input = new Scanner(System.in);
    static ArrayList<Transaction> transactionList = new ArrayList<>();

    public static void main(String[] args) {

        boolean isRunning = true, inLedger;
        String userChoice, ledgerChoice;
        do{
            userChoice = input.nextLine();

            displayHomeMenu();

            switch (userChoice){
                case "D":
                    addDeposit();
                    break;
                case "P":
                    makePayment();
                    break;
                case "L":
                    inLedger = true;

                    while(inLedger) {
                        displayLedgerMenu();
                        ledgerChoice = input.nextLine();
                        switch (ledgerChoice) {
                            case "A":
                                showAllTransacgtion();
                                break;
                            case "D":
                                showDeposits();
                                break;
                            case "P":
                                showPayments();
                                break;
                            case "R":
                                chooseReports();
                                break;
                            case "H":
                                inLedger = false;
                                break;
                            default:
                                println("Invalid Option... TRY AGAIN >:(");

                        }
                    }
                    break;
                case "X":
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
    static void displayHomeMenu(){}
    static void addDeposit(){}
    static void makePayment(){}
    static void displayLedgerMenu(){}
    static void showAllTransacgtion(){}
    static void showDeposits(){}
    static void showPayments(){}
    static void chooseReports(){}
}
