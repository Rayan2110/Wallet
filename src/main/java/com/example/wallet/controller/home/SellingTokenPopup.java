package com.example.wallet.controller.home;

import com.example.wallet.ApiCaller;
import com.example.wallet.entity.CryptoCurrency;
import com.example.wallet.entity.Transaction;
import com.example.wallet.entity.Wallet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.List;

public class SellingTokenPopup {

    private ObservableList<Transaction> transactionData;
    private Object currentIdUser;
    private BigDecimal moneyLeft;
    private float result;
    private Stage stage;
    private Wallet currentWallet;

    public SellingTokenPopup(Wallet currentWallet, BigDecimal moneyLeft, long currentIdUser, ObservableList<Transaction> transactionData) {
        this.currentWallet = currentWallet;
        this.moneyLeft = moneyLeft;
        this.currentIdUser = currentIdUser;
        this.transactionData = transactionData;
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.setTitle("Sell Token ");

        ComboBox<String> comboCryptoMonnaie = new ComboBox<>();
        ObservableMap<String, String> tokenCurrencyMap = FXCollections.observableHashMap();
        for (Transaction transaction : currentWallet.getTransactions()) {
            if (transaction.getTransactionType().equals("PURCHASE_TOKEN") && !comboCryptoMonnaie.getItems().contains(transaction.getToken())) {
                comboCryptoMonnaie.getItems().add(transaction.getIdToken());
            }
        }
        // Ajout d'un écouteur d'événements pour la ComboBox
        comboCryptoMonnaie.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                String currency = tokenCurrencyMap.get(newVal);
                if (currency != null) {
                    fetchCurrentTokenValue(newVal, currency);
                }
            }
        });
        // Ajout des ComboBox au GridPane
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Choose the token you want to sell : "), 0, 0);
        grid.add(comboCryptoMonnaie, 1, 0);
        grid.add(new Label("Current Value of Tokens : "), 0, 1);

        Scene scene = new Scene(grid, 512, 384);
        stage.setScene(scene);

    }


    public float showAndWait() {
        stage.showAndWait();
        return result;
    }

    private Object fetchCurrentTokenValue(String idToken, String currency) {
        ApiCaller apiCaller = new ApiCaller();
        return apiCaller.getPriceOfAnyToken(idToken, currency);
    }
}
