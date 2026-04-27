package com.pluralsight;


import java.time.LocalDateTime;

public class Transaction {

    // Data from .csv, use DateTime for easier / "more precise" sorting
    private LocalDateTime dateTime;
    private String description;
    private String vendor;
    private long coins;

    // Constructor
    Transaction(LocalDateTime currentDateTime, String description, String vendor, long price) {
        this.dateTime = currentDateTime;
        this.description = description;
        this.vendor = vendor;
        this.coins = price;
    }

    // Basic return functions
    public LocalDateTime getDateTime() { return dateTime; }
    public String getDescription() {
        return description;
    }
    public String getVendor() {
        return vendor;
    }
    public long getAmount() {
        return coins;
    }
}