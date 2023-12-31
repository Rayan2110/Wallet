package com.example.wallet.entity;

import java.util.List;

public class User {
    private long id;

    private String mail;

    private String password;

    private String name;

    private String firstName;

    private String age;

    private List<Wallet> wallets;

    public User(long id, String mail, String password, String name, String firstName, String age) {
        this.id = id;
        this.mail = mail;
        this.password = password;
        this.name = name;
        this.firstName = firstName;
        this.age = age;
    }

    public User(String name, String firstName, String age, List<Wallet> wallets) {
        this.name = name;
        this.firstName = firstName;
        this.age = age;
        this.wallets = wallets;
    }

    public User() {

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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
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
