package com.example.wallet.entity;

import java.util.Date;
public class Transaction {

    private long id;

    private String type; //Achat ou vente

    private float price; // prix en euros, dollars

    private Date date;

    private float amount; // prix en terme d'unité ( crypto, action, etc )

    public Transaction(long id, String type, float price, Date date, float amount) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.date = date;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
