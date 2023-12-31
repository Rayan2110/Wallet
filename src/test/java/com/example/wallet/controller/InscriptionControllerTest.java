package com.example.wallet.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InscriptionControllerTest {

    @Test
    public void testIsValidEmailValid() {
        // Adresse e-mail valide
        String validEmail = "test@example.com";
        assertTrue(InscriptionController.isValidEmail(validEmail));
    }

    @Test
    public void testIsValidEmailInvalid() {
        // Adresse e-mail invalide
        String invalidEmail = "invalid-email";
        assertFalse(InscriptionController.isValidEmail(invalidEmail));
    }

    @Test
    public void testIsValidEmailNull() {
        // Adresse e-mail nulle
        String nullEmail = null;
        assertFalse(InscriptionController.isValidEmail(nullEmail));
    }

    @Test
    public void testIsValidEmailEmpty() {
        // Adresse e-mail vide
        String emptyEmail = "";
        assertFalse(InscriptionController.isValidEmail(emptyEmail));
    }
}
