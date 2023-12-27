package com.example.wallet.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GestionUtilisateur {
    String cheminFichier = "src/main/resources/bdd/users.txt"; // Remplacez par le chemin de votre fichier

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

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length < 2) {
                    continue; // Données incomplètes, passe à la ligne suivante
                }

                String emailEnregistre = parts[1];
                String motDePasseEnregistre = parts[2];

                if (emailEnregistre.equals(email) && motDePasseEnregistre.equals(motDePasse)) {
                    return true; // Identifiants corrects
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // Identifiants incorrects
    }
}
