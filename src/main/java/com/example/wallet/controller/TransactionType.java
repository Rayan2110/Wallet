package com.example.wallet.controller;

public enum TransactionType {
    CREATE_WALLET("Création d'un porte-monnaie"),
    DEPOSIT_MONEY("Dépot"),
    PURCHASE_TOKEN("Achat"),
    SALE_TOKEN("Vente"),
    SAVING_MONEY("Épargne");

    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

}
