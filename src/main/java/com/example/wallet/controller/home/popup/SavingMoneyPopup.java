package com.example.wallet.controller.home.popup;

import com.example.wallet.services.GestionTransaction;
import com.example.wallet.controller.TransactionType;
import com.example.wallet.entity.Transaction;
import com.example.wallet.entity.Wallet;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SavingMoneyPopup {
    Wallet currentWallet;
    private float result;
    private TextField numberField;
    BigDecimal moneyLeft;
    long currentIdUser;
    private Stage stage;
    ObservableList<Transaction> transactionData;

    public SavingMoneyPopup(Wallet currentWallet, BigDecimal moneyLeft, long currentIdUser, ObservableList<Transaction> transactionData) {
        this.currentWallet = currentWallet;
        this.moneyLeft = moneyLeft;
        this.currentIdUser = currentIdUser;
        this.transactionData = transactionData;
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        Label labelSaving = new Label("You have in your account : " + moneyLeft + " how much do you want to save ?");
        numberField = new TextField();
        numberField.setPromptText("100");

        // Permet seulement les entrées numériques
        numberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                numberField.setText(newValue.replaceAll("[^\\d.]", ""));
            }
        });

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> handleSubmit());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(labelSaving, numberField, submitButton);

        Scene scene = new Scene(layout, 300, 150);
        stage.setScene(scene);
    }

    private void handleSubmit() {
        try {
            BigDecimal savingAmount = BigDecimal.valueOf(Double.parseDouble(numberField.getText()));

            if (savingAmount.compareTo(moneyLeft) <= 0) {
                Transaction transaction = new Transaction(TransactionType.SAVING_MONEY.name(), savingAmount, currentWallet.getCurrency(), LocalDateTime.now(), BigDecimal.ZERO, null, null);
                GestionTransaction gestionTransaction = new GestionTransaction();
                gestionTransaction.writeTransaction(transaction, currentWallet.getId(), currentIdUser);
                currentWallet.getTransactions().add(transaction);
                transactionData.add(transaction);
                stage.close();
            } else {
                numberField.setText("");
                numberField.setPromptText("Invalid amount: exceeds portfolio balance");
            }
        } catch (NumberFormatException e) {
            numberField.setText("");
            numberField.setPromptText("Please enter a valid number");
        }
    }

    public float showAndWait() {
        stage.showAndWait();
        return result;
    }


}
