package com.example.wallet.controller;

import com.example.wallet.HelloApplication;
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


    @FXML
    public void onReturnButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768); // standard de base
        stage.setScene(scene);
        stage.show();
    }

    @FXML //liaison avec le FXML
    public void onConnexionButtonClick(ActionEvent actionEvent) throws IOException {
        if(passwordField.getText().equals("Rayan2110@") && idField.getText().equals("Rayan2110")){
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("home-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1024, 768); // standard de base
            stage.setScene(scene);
            stage.show();
            error_label.setText("Connexion réussit!");
        }
        else {
            error_label.setText("Erreur! Veuillez Recommencez.");
        }
    }
}
