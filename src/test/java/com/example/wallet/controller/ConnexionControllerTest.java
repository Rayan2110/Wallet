package com.example.wallet.controller;

import com.example.wallet.services.GestionUser;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
public class ConnexionControllerTest {
    private ConnexionController controller;

    @BeforeEach
    public void setUp() {
        controller = new ConnexionController();
        controller.passwordField = new PasswordField();
        controller.idField = new TextField();
        controller.error_label = new Label();
    }

    @Test
    public void testOnConnexionButtonClickFailure() throws IOException {
        // Mock de GestionUser pour simuler une connexion échouée
        GestionUser gestionUserMock = mock(GestionUser.class);
        when(gestionUserMock.verifierIdentifiants(anyString(), anyString())).thenReturn(false);
        controller.gestion = gestionUserMock;

        // Exécute la méthode onConnexionButtonClick
        ActionEvent event = new ActionEvent();
        controller.onConnexionButtonClick(event);

        // Vérifie que le label d'erreur contient le message d'erreur attendu
        assertEquals("Error! Please try again.", controller.error_label.getText());
    }
}
