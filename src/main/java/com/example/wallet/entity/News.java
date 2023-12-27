package com.example.wallet.entity;

import java.util.List;

public class News {

    String Type;

    String Message;

    List<Articles> Data;

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public List<Articles> getData() {
        return Data;
    }

    public void setData(List<Articles> data) {
        Data = data;
    }
}
