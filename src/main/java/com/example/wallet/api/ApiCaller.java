package com.example.wallet.api;

import com.example.wallet.entity.CryptoCurrency;
import com.example.wallet.entity.News;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ApiCaller {
    private static final String BASE_URL_COINGECKO = "https://api.coingecko.com/api/v3";
    private static final String BASE_URL_NEWS = "https://min-api.cryptocompare.com/data/v2/news/?lang=EN";

    private final Gson gson;

    public ApiCaller() {
        this.gson = new Gson();
    }

    public List<CryptoCurrency> getAllCoinsMarket(String currency, int limit, String order, int page, boolean sparkline, String priceChangePercentage, String locale) {
        String url = BASE_URL_COINGECKO + "/coins/markets?vs_currency=" + currency + "&order=" + order + "&per_page=" + limit + "&page=" + page + "&sparkline=" + sparkline
                + "&price_change_percentage=" + priceChangePercentage + "&locale=" + locale;
        String jsonResponse = makeApiCall(url);
        Type listType = new TypeToken<List<CryptoCurrency>>() {
        }.getType();
        return gson.fromJson(jsonResponse, listType);
    }

    public BigDecimal getPriceOfAnyToken(String idToken, String currency) {
        String url = BASE_URL_COINGECKO + "/simple/price?ids=" + idToken + "&vs_currencies=" + currency;
        String jsonResponse = makeApiCall(url);
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
        // Extraction de la valeur en tant que BigDecimal
        BigDecimal price = jsonObject.getAsJsonObject(idToken).get(currency).getAsBigDecimal();
        return price;
    }

    public News getLatestNews() {
        String url = BASE_URL_NEWS;
        String jsonResponse = makeApiCall(url);
        Type listType = new TypeToken<News>() {
        }.getType();
        return gson.fromJson(jsonResponse, listType);
    }

    public String makeApiCall(String apiUrl) {
        try {
            // Crée l'URL de l'API
            URL url = new URL(apiUrl);

            // Ouvre la connexion HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Défini la méthode de requête (GET dans cet exemple)
            connection.setRequestMethod("GET");

            // Obtention de la réponse de l'API
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Lecture de la réponse de l'API
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Retourne la réponse JSON
                return response.toString();
            } else {
                // Gére les erreurs de l'API
                System.out.println("Erreur lors de la requête. Code de réponse : " + responseCode);
                return null;
            }
        } catch (IOException e) {
            // Gére les exceptions d'entrée/sortie
            e.printStackTrace();
            return null;
        }
    }

}
