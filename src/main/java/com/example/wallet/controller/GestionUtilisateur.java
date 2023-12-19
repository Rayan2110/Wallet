package com.example.wallet.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GestionUtilisateur {
    String cheminFichier = "src/main/resources/bdd/logs.txt"; // Remplacez par le chemin de votre fichier

    public void inscrireUtilisateur(String email, String motDePasse, String name, String firstName, String birthday) {
        try (FileWriter writer = new FileWriter(cheminFichier, true)) {
            writer.write(email + "/" + motDePasse + "/" + name + "/" + firstName + "/" + birthday + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour vérifier les identifiants lors de la connexion
    public boolean verifierIdentifiants(String email, String motDePasse) {
        try (BufferedReader reader = new BufferedReader(new FileReader(cheminFichier))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("/");
                if (parts.length < 2) {
                    continue; // Données incomplètes, passe à la ligne suivante
                }

                String emailEnregistre = parts[0];
                String motDePasseEnregistre = parts[1];

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
