package com.example.wallet.entity;

import java.util.List;

public class User {
    private long id;

    private String mail;

    private String password;

    private String name;

    private String firstName;

    private int age;

    private List<Wallet> wallets;

    public User(long id, String name, String firstName, int age, List<Wallet> wallets) {
        this.id = id;
        this.name = name;
        this.firstName = firstName;
        this.age = age;
        this.wallets = wallets;
    }

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Wallet> getWallets() {
        return wallets;
    }

    public void setWallets(List<Wallet> wallets) {
        this.wallets = wallets;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
