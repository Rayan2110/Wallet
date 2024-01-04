package com.example.wallet.entity;

import java.time.LocalDateTime;

public class Transaction {

    private long id;

    private String transactionType; //Achat ou vente

    private float price; // prix en euros, dollars

    private LocalDateTime date;

    private double amount; // prix en terme d'unité ( crypto, action, etc )

    private String token;

    public Transaction(long id, String transactionType, float price, LocalDateTime date, float amount, String token) {
        this.id = id;
        this.transactionType = transactionType;
        this.price = price;
        this.date = date;
        this.amount = amount;
        this.token = token;
    }

    public Transaction(String transactionType, float price, LocalDateTime date, double amount, String token) {
        this.transactionType = transactionType;
        this.price = price;
        this.date = date;
        this.amount = amount;
        this.token = token;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
