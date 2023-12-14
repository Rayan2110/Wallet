package com.example.wallet.controller;

import com.example.wallet.apicaller.ApiCaller;
import com.example.wallet.entity.CryptoCurrency;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    private TableView<CryptoCurrency> tableView; // Assure-toi que tu as bien défini TableView dans ton fichier FXML

    @FXML
    private TableColumn<CryptoCurrency, String> nameCol;

    @FXML
    private TableColumn<CryptoCurrency, String> symbolCol;

    @FXML
    private TableColumn<CryptoCurrency, Double> priceCol;

    @FXML
    private TableColumn<CryptoCurrency, Long> marketCapCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Appeler l'API et récupérer les données
        System.out.println("dede");
        ApiCaller apiCaller = new ApiCaller();
        List<CryptoCurrency> allCoinsMarket = apiCaller.getAllCoinsMarket("usd", 10, "market_cap_desc", 1, true, "7d", "en");
        System.out.println(allCoinsMarket);
        List<CryptoCurrency> cryptoData = fetchDataFromApi();

        // Configurer les colonnes de la TableView
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        symbolCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSymbol()));
        priceCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getCurrentPrice()).asObject());
        marketCapCol.setCellValueFactory(data -> new SimpleLongProperty(data.getValue().getMarketCap()).asObject());

        // Charger les données dans la TableView
        tableView.getItems().addAll(cryptoData);
    }

    // Méthode pour appeler l'API et récupérer les données
    private List<CryptoCurrency> fetchDataFromApi() {
        return Collections.emptyList(); // Remplace par les vraies données de l'API
    }
}
