package com.example.wallet.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {

    private long id;

    private String transactionType; //Achat ou vente


    private BigDecimal price; // prix en euros, dollars

    private String currency;

    private LocalDateTime date;

    private BigDecimal amount; // prix en terme d'unité ( crypto, action, etc )

    private String token;
    private String idToken;

    public Transaction(long id, String transactionType, BigDecimal price, String currency, LocalDateTime date, BigDecimal amount, String token, String idToken) {
        this.id = id;
        this.transactionType = transactionType;
        this.price = price;
        this.currency = currency;
        this.date = date;
        this.amount = amount;
        this.token = token;
        this.idToken = idToken;
    }

    public Transaction(String transactionType, BigDecimal price, String currency, LocalDateTime date, BigDecimal amount, String token, String idToken) {
        this.transactionType = transactionType;
        this.price = price;
        this.currency = currency;
        this.date = date;
        this.amount = amount;
        this.token = token;
        this.idToken = idToken;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
