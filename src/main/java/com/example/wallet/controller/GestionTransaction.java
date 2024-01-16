package com.example.wallet.controller;

import com.example.wallet.entity.Transaction;
import com.example.wallet.entity.User;
import com.example.wallet.entity.Wallet;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class GestionTransaction {

    private String cheminFichier = "src/main/resources/bdd/transactions.txt";

    public void writeTransaction(Transaction transaction, long idWallet, long idUser) {
        try (FileWriter writer = new FileWriter(cheminFichier, true)) {
            long id = nbTransaction();
            transaction.setId(id);
            writer.write(id + "|" + idUser + "|" + transaction.getTransactionType() + "|" + transaction.getPrice() + "|" + transaction.getCurrency().toUpperCase() + "|" + transaction.getDate() + "|" + transaction.getAmount() + "|" + transaction.getToken() + "|" + transaction.getIdToken() + "|" + idWallet + "\n");
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
                    continue;
                }
                String idUser = parts[1];

                if (idUser.equals(String.valueOf(currentUser.getId()))) {
                    Transaction transaction = new Transaction(Long.parseLong(parts[0]), parts[2], BigDecimal.valueOf(Double.parseDouble(parts[3])), parts[4], LocalDateTime.parse(parts[5]), BigDecimal.valueOf(Float.parseFloat(parts[6])), parts[7], parts[8]);
                    long idWalletOfTransaction = Long.parseLong(parts[9]);
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
