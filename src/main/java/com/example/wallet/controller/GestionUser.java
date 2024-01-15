package com.example.wallet.controller;

import com.example.wallet.entity.User;
import com.example.wallet.entity.Wallet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class GestionUser {
    private static GestionUser instance;
    private String cheminFichier = "src/main/resources/bdd/users.txt";
    private User currentUser;

    public static GestionUser getInstance() {
        if (instance == null) {
            instance = new GestionUser();
        }
        return instance;
    }

    public void inscrireUtilisateur(String email, String motDePasse, String name, String firstName, String birthday) {
        try (FileWriter writer = new FileWriter(cheminFichier, true)) {
            int id = nbUser();
            writer.write(id + "|" + email + "|" + motDePasse + "|" + name + "|" + firstName + "|" + birthday + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int nbUser() {
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

    // Méthode pour vérifier les identifiants lors de la connexion
    public boolean verifierIdentifiants(String email, String motDePasse) {
        try (BufferedReader reader = new BufferedReader(new FileReader(cheminFichier))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length < 2) {
                    continue; // Données incomplètes, passe à la ligne suivante
                }

                String emailEnregistre = parts[1];
                String motDePasseEnregistre = parts[2];

                if (emailEnregistre.equals(email) && motDePasseEnregistre.equals(motDePasse)) {
                    User user = new User(Long.parseLong(parts[0]), parts[1], parts[2], parts[3], parts[4], parts[5]);
                    GestionWallet gestionWallet = new GestionWallet();
                    List<Wallet> wallets = gestionWallet.checkMyWallets(Long.parseLong(parts[0]));
                    if (wallets != null && wallets.size() > 0) {
                        System.out.println("tu as plusieurs wallets");
                        user.setWallets(wallets);
                    }
                    GestionUser.getInstance().setCurrentUser(user);
                    return true; // Identifiants corrects
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // Identifiants incorrects
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
