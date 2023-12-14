package com.example.wallet.apicaller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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

    public List<NewsArticle> getLatestNews() {
        String url = "https://min-api.cryptocompare.com/data/v2/news/?lang=EN"; // Replace with the actual URL for news
        String jsonResponse = makeApiCall(url);
        Type listType = new TypeToken<List<NewsArticle>>() {
        }.getType();
        return gson.fromJson(jsonResponse, listType);
    }

    private String makeApiCall(String apiUrl) {
        // Code to make the API call and get the JSON response (use HttpURLConnection, OkHttp, etc.)
        // Return the JSON response as a string
        return ""; // Replace with actual implementation
    }

    // Define the data classes for deserialization
    private static class CryptoCurrency {
        // Define fields based on the structure of the JSON response
    }

    private static class NewsArticle {
        // Define fields based on the structure of the JSON response from the news API
    }
}
