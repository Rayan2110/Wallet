package com.example.wallet.controller;

import com.example.wallet.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GestionUserTest {
    private GestionUser gestionUser;

    @BeforeEach
    public void setUp() {
        gestionUser = new GestionUser();
    }

    @Test
    public void testInscrireUtilisateur() {
        String email = "test@example.com";
        String motDePasse = "password";
        String name = "John";
        String firstName = "Doe";
        String birthday = "1990/01/01";

        gestionUser.inscrireUtilisateur(email, motDePasse, name, firstName, birthday);

        // You can add assertions to check if the user was added successfully to the file.
        // You may need to read the file and verify its contents.
    }

    @Test
    public void testVerifierIdentifiants() {
        String email = "test@example.com";
        String motDePasse = "password";

        boolean result = gestionUser.verifierIdentifiants(email, motDePasse);

        // You can add assertions to check if the verification of credentials works as expected.
        // You may need to mock the file data or provide a test-specific data file.
        assertTrue(result); // Assuming the provided credentials are valid for the test data.
    }

    // Add more tests for other methods of GestionUser as needed.

    @Test
    public void testGetCurrentUser() {
        User user = new User(1, "test@example.com", "password", "John", "Doe", "1990-01-01");
        gestionUser.setCurrentUser(user);

        User currentUser = gestionUser.getCurrentUser();

        assertEquals(user, currentUser);
    }

    @Test
    public void testSetCurrentUser() {
        User user = new User(1, "test@example.com", "password", "John", "Doe", "1990-01-01");
        gestionUser.setCurrentUser(user);

        User currentUser = gestionUser.getCurrentUser();

        assertEquals(user, currentUser);
    }
}
