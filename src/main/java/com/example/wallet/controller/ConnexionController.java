package com.example.wallet.controller;

import com.example.wallet.HelloApplication;
import com.example.wallet.services.GestionTransaction;
import com.example.wallet.services.GestionUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

import static com.example.wallet.HelloApplication.stage;

public class ConnexionController {

    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField idField;
    @FXML
    public Label error_label;

    public GestionUser gestion;
    public GestionTransaction gestionTransaction;


    @FXML
    public void onReturnButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768); // standard de base
        stage.setScene(scene);
        stage.show();
    }

    @FXML //liaison avec le FXML
    public void onConnexionButtonClick(ActionEvent actionEvent) throws IOException {
        gestion = new GestionUser();
        boolean connexionReussie = gestion.verifierIdentifiants(idField.getText(), passwordField.getText());
        if (connexionReussie) {
            System.out.println("Successful connection!");
            gestionTransaction = new GestionTransaction();
            gestionTransaction.getAllTransactions(GestionUser.getInstance().getCurrentUser());
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("home-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1024, 768); // standard de base
            stage.setScene(scene);
            stage.show();
        } else {
            error_label.setText("Error! Please try again.");
        }
    }

}
