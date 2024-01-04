package com.example.wallet.controller.home;

import com.example.wallet.entity.CryptoCurrency;
import com.example.wallet.entity.Wallet;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PurchaseTokenPopup {
    private Stage stage;
    private TextField numberField;
    private Label labelToken;
    private Label price;
    private float result;
    private Wallet currentWallet;
    private CryptoCurrency crypto;


    public PurchaseTokenPopup(CryptoCurrency crypto, Wallet currentWallet) {
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
            if (!newValue.matches("\\d*")) {
                numberField.setText(newValue.replaceAll("[^\\d]", ""));
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

            if (investmentAmount <= currentWallet.getMoney()) {
                result = investmentAmount;
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
