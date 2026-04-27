package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;

public class Transaction {
    private LocalDate date;
    private LocalTime time;
    private String description;
    private String vendor;
    private long coins;

    Transaction(LocalDate date, LocalTime time, String description, String vendor, long price) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.coins = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
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