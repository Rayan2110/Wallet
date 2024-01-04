package com.example.wallet.controller.home;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PurchaseTokenPopup {
    private Stage stage;
    private TextField numberField;
    private double result;

    public PurchaseTokenPopup() {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Entrée Numérique");

        numberField = new TextField();
        numberField.setPromptText("Entrez un nombre");

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
            result = Double.parseDouble(numberField.getText());
            stage.close();
        } catch (NumberFormatException e) {
            numberField.setText("");
            numberField.setPromptText("Veuillez entrer un nombre valide");
        }
    }

    public double showAndWait() {
        stage.showAndWait();
        return result;
    }
}
