package com.example.wallet.controller;

import com.example.wallet.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

import static com.example.wallet.HelloApplication.stage;


public class InscriptionController {
    @FXML
    public Label descriptionLabel;

    @FXML
    public TextField nameField;

    @FXML
    public TextField firstNameField;

    @FXML
    public TextField birthdayField;

    @FXML
    public TextField mailField;

    @FXML
    public TextField passwordField;


    public InscriptionController() throws IOException {
    }

    @FXML
    public void onReturnButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768); // standard de base
        stage.setScene(scene);
        stage.show();
    }

    public void onInscriptionButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("connexion-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768); // standard de base
        descriptionLabel.setText("Inscription réussit !");
        GestionUtilisateur gestion = new GestionUtilisateur();

        gestion.inscrireUtilisateur(mailField.getText(), passwordField.getText(), nameField.getText(), firstNameField.getText(), birthdayField.getText());
    }
}


