package com.example.wallet.controller;

import com.example.wallet.entity.Transaction;
import com.example.wallet.entity.User;
import com.example.wallet.entity.Wallet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class GestionTransaction {

    private String cheminFichier = "src/main/resources/bdd/transactions.txt"; // Remplacez par le chemin de votre fichier

    public void writeTransaction(Transaction transaction, long idWallet, long idUser) {
        try (FileWriter writer = new FileWriter(cheminFichier, true)) {
            long id = nbTransaction();
            transaction.setId(id);
            writer.write(id + "|" + idUser + "|" + transaction.getTransactionType() + "|" + transaction.getPrice() + "|" + transaction.getDate() + "|" + transaction.getAmount() + "|" + transaction.getToken() + "|" + idWallet + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long nbTransaction() {
        long lineCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(cheminFichier))) {
            while (reader.readLine() != null) {
                lineCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lineCount;
    }

    // Méthode pour filtrer les transactions par identifiant de portefeuille

    public void getAllTransactions(User currentUser) {
        try (BufferedReader reader = new BufferedReader(new FileReader(cheminFichier))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length < 7) {
                    continue; // Données incomplètes, passe à la ligne suivante
                }
                String idUser = parts[1];
                String idWallet = parts[7];

                if (idUser.equals(String.valueOf(currentUser.getId()))) {
                    Transaction transaction = new Transaction(Long.parseLong(parts[0]), parts[2], Float.parseFloat(parts[3]), LocalDateTime.parse(parts[4]), Float.parseFloat(parts[5]), parts[6]);
                    long idWalletOfTransaction = Long.parseLong(parts[7]);
                    for (Wallet wallet : currentUser.getWallets()) {
                        if (wallet.getId() == idWalletOfTransaction) {
                            wallet.getTransactions().add(transaction);
                        }
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
