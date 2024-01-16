package com.example.wallet.controller.home;

import com.example.wallet.ApiCaller;
import com.example.wallet.entity.Transaction;
import com.example.wallet.entity.Wallet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicReference;

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
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.setTitle("Sell Token ");

        ComboBox<String> comboCryptoMonnaie = new ComboBox<>();
        ObservableMap<String, String> tokenCurrencyMap = FXCollections.observableHashMap();
        for (Transaction transaction : currentWallet.getTransactions()) {
            if (transaction.getTransactionType().equals("PURCHASE_TOKEN")) {
                // Supposons que getCurrency() retourne la monnaie du token.
                tokenCurrencyMap.put(transaction.getIdToken(), transaction.getCurrency());
                if (!comboCryptoMonnaie.getItems().contains(transaction.getIdToken())) {
                    comboCryptoMonnaie.getItems().add(transaction.getIdToken());
                }
            }
        }
        // Ajout d'un écouteur d'événements pour la ComboBox
        AtomicReference<BigDecimal> currentValueToken = new AtomicReference<>(BigDecimal.ZERO);
        Label labelCurrentValueToken = new Label();
        Label labelAccountToken = new Label();
        Label labelnbSellingToken = new Label();
        Button submitButton = new Button("Submit");
        AtomicReference<TextField> numberField = new AtomicReference<>();
        // Initialiser numberField avec un nouveau TextField
        numberField.set(new TextField());
        comboCryptoMonnaie.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                String currency = tokenCurrencyMap.get(newVal);
                if (currency != null) {
                    currentValueToken.set(fetchCurrentTokenValue(newVal, currency));
                    labelCurrentValueToken.setText(currentValueToken.toString());

                    BigDecimal purchase_token = transactionData.stream()
                            // Filtrer par transactionType
                            .filter(transaction -> transaction.getTransactionType().equals("PURCHASE_TOKEN") && transaction.getIdToken().equals(newVal))
                            // Convertir chaque transaction filtrée en son prix
                            .map(Transaction::getPrice)
                            // Somme des prix
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    labelAccountToken.setText("You have in your account " + purchase_token.toString());
                    labelnbSellingToken.setText("How much do you want to sell ? ");
                    // Permet seulement les entrées numériques
                    numberField.get().textProperty().addListener((observable, oldValue, newValue) -> {
                        if (!newValue.matches("\\d*(\\.\\d*)?")) {
                            numberField.get().setText(newValue.replaceAll("[^\\d.]", ""));
                        }
                    });

                    submitButton.setOnAction(e -> handleSellingSubmit(numberField,purchase_token,currentValueToken));

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
        grid.add(labelCurrentValueToken, 1, 1);
        grid.add(labelAccountToken, 0, 2);
        grid.add(labelnbSellingToken, 0, 3);
        grid.add(numberField.get(), 1, 3);
        grid.add(submitButton, 0, 4);

        Scene scene = new Scene(grid, 512, 384);
        stage.setScene(scene);

    }

    private void handleSellingSubmit(AtomicReference<TextField> nbToken, BigDecimal purchase_token, AtomicReference<BigDecimal> currentValueToken) {
        BigDecimal nbTokensPrice = BigDecimal.valueOf(Double.parseDouble(nbToken.get().getText()));
        if (nbTokensPrice.compareTo(purchase_token)<=0) {
            // Calculer la valeur actuelle de votre investissement
            BigDecimal quantityOfCrypto = nbTokensPrice.divide(currentValueToken.get(),2, RoundingMode.HALF_UP);
            BigDecimal currentValue = quantityOfCrypto.multiply(currentValueToken.get());

            // Comparer la valeur actuelle avec l'investissement initial
            int comparisonResult = currentValue.compareTo(nbTokensPrice);

            // Déterminer si vous êtes rentable
            if (comparisonResult > 0) {
                System.out.println("Profitable: Your investment has increased.");
            } else if (comparisonResult < 0) {
                System.out.println("Unprofitable: Your investment has decreased.");
            } else {
                System.out.println("Balanced: Your investment is equal to your initial investment.");
            }
        }
    }


    public float showAndWait() {
        stage.showAndWait();
        return result;
    }

    private BigDecimal fetchCurrentTokenValue(String idToken, String currency) {
        ApiCaller apiCaller = new ApiCaller();
        return apiCaller.getPriceOfAnyToken(idToken, currency.toLowerCase());
    }
}
