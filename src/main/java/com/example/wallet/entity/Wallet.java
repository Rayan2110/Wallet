package com.example.wallet.entity;

import java.time.LocalDateTime;
import java.util.List;

public class Wallet {

    private long id;

    private String name;

    private String title;

    private String description;

    private float money;

    private List<Transaction> transactions;

    private LocalDateTime creationDate;

    private History history;

    private String currency;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public History getHistory() {
        return history;
    }

    public void setHistory(History history) {
        this.history = history;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Wallet(long id, String name, String title, String description, float money, LocalDateTime creationDate, String currency) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.description = description;
        this.money = money;
        this.creationDate = creationDate;
        this.currency = currency;
    }
}
