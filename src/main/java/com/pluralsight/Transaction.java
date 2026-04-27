package com.pluralsight;


import java.time.LocalDateTime;

public class Transaction {
    private LocalDateTime dateTime;
    private String description;
    private String vendor;
    private long coins;

    Transaction(LocalDateTime currentDateTime, String description, String vendor, long price) {
        this.dateTime = currentDateTime;
        this.description = description;
        this.vendor = vendor;
        this.coins = price;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

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