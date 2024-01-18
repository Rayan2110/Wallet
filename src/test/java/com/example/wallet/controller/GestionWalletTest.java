package com.example.wallet.controller;

import com.example.wallet.entity.Wallet;
import com.example.wallet.services.GestionWallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.time.LocalDateTime;

public class GestionWalletTest {
    private GestionWallet gestionWallet;

    @BeforeEach
    public void setUp() {
        gestionWallet = new GestionWallet();
    }

    @Test
    public void testNewWallet() {
        Wallet wallet = new Wallet(1, "Wallet 1", "Description 1", BigDecimal.valueOf(100.0), LocalDateTime.now(), "USD");
        long userId = 1;
        gestionWallet.newWallet(wallet, userId);

    }

    @Test
    public void testCheckMyWallets() {
        long userId = 1;

        List<Wallet> wallets = gestionWallet.checkMyWallets(userId);

    }


}
