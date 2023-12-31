package com.example.wallet.controller.home;

import com.example.wallet.controller.GestionTransaction;
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

import java.time.LocalDateTime;


public class PurchaseTokenPopup {
    private float moneyLeft;
    private Stage stage;
    private TextField numberField;
    private Label labelToken;
    private Label price;
    private float result;
    private Wallet currentWallet;
    private CryptoCurrency crypto;
    private double amount;
    private long idUser;
    private ObservableList<Transaction> transactionData;

    public PurchaseTokenPopup(CryptoCurrency crypto, Wallet currentWallet, float moneyLeft, long idUser, ObservableList<Transaction> transactionData) {
        this.idUser = idUser;
        this.moneyLeft = moneyLeft;
        this.transactionData = transactionData;
        this.crypto = crypto;
        this.currentWallet = currentWallet;
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Achat de token " + crypto.getName());


        labelToken = new Label("Token : " + crypto.getSymbol() + " Prix : " + crypto.getCurrentPrice());
        numberField = new TextField();
        numberField.setPromptText("Combien voulez-vous investir");

        // Permet seulement les entrées numériques
        numberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                numberField.setText(newValue.replaceAll("[^\\d.]", ""));
            }
        });

        Button submitButton = new Button("Soumettre");
        submitButton.setOnAction(e -> handleSubmit());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(numberField, submitButton);

        Scene scene = new Scene(layout, 300, 150);
        stage.setScene(scene);
    }

    private void handleSubmit() {
        try {
            float investmentAmount = Float.parseFloat(numberField.getText());

            if (investmentAmount <= moneyLeft) {
                this.amount = investmentAmount / crypto.getCurrentPrice();
                Transaction transaction = new Transaction(TransactionType.PURCHASE_TOKEN.name(), investmentAmount, LocalDateTime.now(), amount, crypto.getSymbol());
                GestionTransaction gestionTransaction = new GestionTransaction();
                gestionTransaction.writeTransaction(transaction, currentWallet.getId(), idUser);
                currentWallet.getTransactions().add(transaction);
                transactionData.add(transaction);
                stage.close();
            } else {
                numberField.setText("");
                numberField.setPromptText("Montant invalide : dépasse le solde du portefeuille");
            }
        } catch (NumberFormatException e) {
            numberField.setText("");
            numberField.setPromptText("Veuillez entrer un nombre valide");
        }
    }

    public float showAndWait() {
        stage.showAndWait();
        return result;
    }
}
