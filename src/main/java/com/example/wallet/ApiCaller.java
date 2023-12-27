package com.example.wallet;

import com.example.wallet.entity.CryptoCurrency;
import com.example.wallet.entity.News;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ApiCaller {
    private static final String BASE_URL = "https://api.coingecko.com/api/v3";

    private final Gson gson;

    public ApiCaller() {
        this.gson = new Gson();
    }

    public List<CryptoCurrency> getAllCoinsMarket(String currency, int limit, String order, int page, boolean sparkline, String priceChangePercentage, String locale) {
        String url = BASE_URL + "/coins/markets?vs_currency=" + currency + "&order=" + order + "&per_page=" + limit + "&page=" + page + "&sparkline=" + sparkline
                + "&price_change_percentage=" + priceChangePercentage + "&locale=" + locale;
        String jsonResponse = makeApiCall(url);
        Type listType = new TypeToken<List<CryptoCurrency>>() {
        }.getType();
        return gson.fromJson(jsonResponse, listType);
    }

    public List<News> getLatestNews() {
        String url = "https://min-api.cryptocompare.com/data/v2/news/?lang=EN"; // Replace with the actual URL for news
        String jsonResponse = makeApiCall(url);
        Type listType = new TypeToken<List<News>>() {
        }.getType();
        return gson.fromJson(jsonResponse, listType);
    }

    public String makeApiCall(String apiUrl) {
        try {
            // Créer l'URL de l'API
            URL url = new URL(apiUrl);

            // Ouvrir la connexion HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Définir la méthode de requête (GET dans cet exemple)
            connection.setRequestMethod("GET");

            // Obtenir la réponse de l'API
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Lire la réponse de l'API
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Retourner la réponse JSON
                return response.toString();
            } else {
                // Gérer les erreurs de l'API
                System.out.println("Erreur lors de la requête. Code de réponse : " + responseCode);
                return null;
            }
        } catch (IOException e) {
            // Gérer les exceptions d'entrée/sortie
            e.printStackTrace();
            return null;
        }
    }

}
