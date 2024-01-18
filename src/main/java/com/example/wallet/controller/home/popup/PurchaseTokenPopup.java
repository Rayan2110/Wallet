package com.example.wallet.controller.home.popup;

import com.example.wallet.services.GestionTransaction;
import com.example.wallet.controller.TransactionType;
import com.example.wallet.entity.CryptoCurrency;
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
import java.math.RoundingMode;
import java.time.LocalDateTime;


public class PurchaseTokenPopup {
    private BigDecimal moneyLeft;
    private Stage stage;
    private TextField numberField;
    private Label labelToken;
    private float result;
    private Wallet currentWallet;
    private CryptoCurrency crypto;
    private BigDecimal amount;
    private long idUser;
    private ObservableList<Transaction> transactionData;

    public PurchaseTokenPopup(CryptoCurrency crypto, Wallet currentWallet, BigDecimal moneyLeft, long idUser, ObservableList<Transaction> transactionData) {
        this.idUser = idUser;
        this.moneyLeft = moneyLeft;
        this.transactionData = transactionData;
        this.crypto = crypto;
        this.currentWallet = currentWallet;
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Buy token " + crypto.getName());

        labelToken = new Label("Token : " + crypto.getSymbol() + " Price : " + crypto.getCurrentPrice());
        numberField = new TextField();
        numberField.setPromptText("How much do you want to invest ? ");

        // Permet seulement les entrées numériques
        numberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                numberField.setText(newValue.replaceAll("[^\\d.]", ""));
            }
        });

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> handleSubmit());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(numberField, submitButton);

        Scene scene = new Scene(layout, 300, 150);
        stage.setScene(scene);
    }

    private void handleSubmit() {
        try {
            BigDecimal investmentAmount = BigDecimal.valueOf(Double.parseDouble(numberField.getText()));

            if (investmentAmount.compareTo(moneyLeft) <= 0) {
                this.amount = investmentAmount.divide(crypto.getCurrentPrice(), 2, RoundingMode.HALF_UP);
                Transaction transaction = new Transaction(TransactionType.PURCHASE_TOKEN.name(), investmentAmount, currentWallet.getCurrency(), LocalDateTime.now(), amount, crypto.getSymbol(), crypto.getId());
                GestionTransaction gestionTransaction = new GestionTransaction();
                gestionTransaction.writeTransaction(transaction, currentWallet.getId(), idUser);
                currentWallet.getTransactions().add(transaction);
                transactionData.add(transaction);
                stage.close();
            } else {
                numberField.setText("");
                numberField.setPromptText("Invalid amount: exceeds wallet balance");
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
