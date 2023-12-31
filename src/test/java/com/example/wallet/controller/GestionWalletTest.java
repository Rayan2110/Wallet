package com.example.wallet.controller;

import com.example.wallet.entity.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class GestionWalletTest {
    private GestionWallet gestionWallet;

    @BeforeEach
    public void setUp() {
        gestionWallet = new GestionWallet();
    }

    @Test
    public void testNewWallet() {
        Wallet wallet = new Wallet(1, "Wallet 1", "Description 1", 100.0f, LocalDateTime.now(), "USD");
        long userId = 1; // Replace with a valid user ID
        gestionWallet.newWallet(wallet, userId);

        // You can add assertions to check if the wallet was added successfully to the file.
        // You may need to read the file and verify its contents.
    }

    @Test
    public void testCheckMyWallets() {
        long userId = 1; // Replace with a valid user ID

        List<Wallet> wallets = gestionWallet.checkMyWallets(userId);

        // You can add assertions to check if the returned list of wallets contains the expected wallets for the user.
        // You may need to read the file and filter the wallets based on the user ID.
    }

    // Add more tests for other methods of GestionWallet as needed.
}
