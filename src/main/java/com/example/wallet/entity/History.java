package com.example.wallet.entity;

import java.util.Date;

public class History {

    private Date date;

    private float description;

    private float value;

    public History(Date date, float description, float value) {
        this.date = date;
        this.description = description;
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getDescription() {
        return description;
    }

    public void setDescription(float description) {
        this.description = description;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
