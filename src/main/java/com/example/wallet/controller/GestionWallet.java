package com.example.wallet.controller;

import com.example.wallet.entity.User;
import com.example.wallet.entity.Wallet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GestionWallet {
    String cheminFichier = "src/main/resources/bdd/wallets.txt"; // Remplacez par le chemin de votre fichier
    Wallet currentWallet;

    public void newWallet(Wallet wallet, int idUser) {
        try (FileWriter writer = new FileWriter(cheminFichier, true)) {
            int id = nbWallet();
            writer.write(id + "|" + wallet.getTitle() + "|" + wallet.getDescription() + "|" + wallet.getMoney() + "|" + LocalDateTime.now() + "|" + wallet.getCurrency() + "|" + idUser + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int nbWallet() {
        int lineCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(cheminFichier))) {
            while (reader.readLine() != null) {
                lineCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lineCount;
    }

    public List<Wallet> checkMyWallets(long idUser) {
        try (BufferedReader reader = new BufferedReader(new FileReader(cheminFichier))) {
            String line;
            List<Wallet> wallets = new ArrayList<>();
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length < 2) {
                    continue; // Données incomplètes, passe à la ligne suivante
                }
                //Id|Title|Description|Money|DateOfCreation|Currency|IdUser

                long idUserRegister = Long.parseLong(parts[6]);

                if (idUserRegister == idUser) {
                    Wallet wallet = new Wallet(Long.parseLong(parts[0]),parts[1],parts[2],parts[3],Float.parseFloat(parts[4]),LocalDateTime.parse(parts[5]),parts[6]);
                    wallets.add(wallet);
                }
            }
            return wallets;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
