package com.example.wallet.controller;

import com.example.wallet.entity.User;
import com.example.wallet.services.GestionUser;
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

    }

    @Test
    public void testVerifierIdentifiants() {
        String email = "test@example.com";
        String motDePasse = "password";

        boolean result = gestionUser.verifierIdentifiants(email, motDePasse);

        assertTrue(result);
    }


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
