package com.example.wallet.controller;

import com.example.wallet.apicaller.ApiCaller;
import com.example.wallet.entity.CryptoCurrency;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;

import java.net.URL;
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
        // Image Column
        TableColumn<CryptoCurrency, Image> imageCol = new TableColumn<>("Image");

        List<CryptoCurrency> cryptoDataApi = fetchDataFromApi();
        ObservableList<CryptoCurrency> cryptoData = FXCollections.observableArrayList();
        cryptoData.addAll(cryptoDataApi);
        // Ajoute des boutons de tri sur les colonnes
        addSortButtons(nameCol);
        addSortButtons(symbolCol);
        addSortButtons(priceCol);
        addSortButtons(marketCapCol);

        // Currency Column with a dropdown button
        TableColumn<CryptoCurrency, String> currencyCol = new TableColumn<>("Currency");
        // Implement a custom cell factory with a dropdown button for currency selection
        // Last 7 Days Column
        TableColumn<CryptoCurrency, String> last7DaysCol = new TableColumn<>("Last 7 Days");
        //last7DaysCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSparklineIn7d()));

        // 24h Volume Column
        TableColumn<CryptoCurrency, Long> volume24hCol = new TableColumn<>("24h Volume");
        volume24hCol.setCellValueFactory(data -> new SimpleLongProperty(data.getValue().getTotalVolume()).asObject());

        // Ajoute les colonnes à la TableView
        // Utilise les colonnes déclarées au niveau de la classe
        imageCol.setCellValueFactory(data -> new SimpleObjectProperty<>(new Image(data.getValue().getImage())));
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        symbolCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSymbol()));
        priceCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getCurrentPrice()).asObject());
        marketCapCol.setCellValueFactory(data -> new SimpleLongProperty(data.getValue().getMarketCap()).asObject());

        // Utilise setItems sur l'instance existante
        tableView.setItems(cryptoData);
    }

    // Méthode pour appeler l'API et récupérer les données
    private List<CryptoCurrency> fetchDataFromApi() {
        ApiCaller apiCaller = new ApiCaller();
        return apiCaller.getAllCoinsMarket("usd", 10, "market_cap_desc", 1, true, "7d", "en");
    }

    private void addSortButtons(TableColumn<CryptoCurrency, ?> column) {
        // Ajoute un bouton de tri croissant
        Button sortAscButton = new Button("▲");
        sortAscButton.getStyleClass().add("sort-button"); // Ajoute la classe CSS
        sortAscButton.setMinSize(10, 10); // Ajuste la taille du bouton
        sortAscButton.setOnAction(event -> {
            tableView.getSortOrder().clear(); // Clear existing sort order
            tableView.getSortOrder().add(column); // Add the current column for ascending order
            column.setSortType(TableColumn.SortType.ASCENDING);
        });

        // Ajoute un bouton de tri décroissant
        Button sortDescButton = new Button("▼");
        sortDescButton.getStyleClass().add("sort-button"); // Ajoute la classe CSS
        sortDescButton.setMinSize(10, 10); // Ajuste la taille du bouton
        sortDescButton.setOnAction(event -> {
            tableView.getSortOrder().clear();
            tableView.getSortOrder().add(column);
            column.setSortType(TableColumn.SortType.DESCENDING);
        });

        // Ajoute les boutons au header de la colonne
        column.setGraphic(new HBox(sortAscButton, sortDescButton));
        column.setSortable(true); // Rend la colonne triable
    }

}
