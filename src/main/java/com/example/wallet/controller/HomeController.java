package com.example.wallet.controller;

import com.example.wallet.apicaller.ApiCaller;
import com.example.wallet.entity.CryptoCurrency;
import com.example.wallet.entity.News;
import com.example.wallet.entity.Sparkline;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    private TableView<CryptoCurrency> tableView; // Assure-toi que tu as bien défini TableView dans ton fichier FXML

    @FXML
    private TableColumn<CryptoCurrency, String> imageCol;

    @FXML
    private TableColumn<CryptoCurrency, String> nameCol;

    @FXML
    private TableColumn<CryptoCurrency, String> symbolCol;

    @FXML
    private TableColumn<CryptoCurrency, Double> priceCol;

    @FXML
    private TableColumn<CryptoCurrency, Long> marketCapCol;

    @FXML
    private TableColumn<CryptoCurrency, Sparkline> last7dCol;

    @FXML
    private Button addMoney;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        List<CryptoCurrency> cryptoDataApi = fetchDataFromApi();
        News cryptoNewsApi = fetchNewsFromApi();
        ObservableList<CryptoCurrency> cryptoData = FXCollections.observableArrayList();
        cryptoData.addAll(cryptoDataApi);

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
        imageCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getImage()));
        imageCol.setCellFactory(column -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);

                if (empty || imagePath == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Image image = new Image(imagePath);
                    imageView.setImage(image);
                    imageView.setFitHeight(30);
                    imageView.setFitWidth(30);
                    setGraphic(imageView);
                }
            }
        });

        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        symbolCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSymbol()));
        priceCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getCurrentPrice()).asObject());
        marketCapCol.setCellValueFactory(data -> new SimpleLongProperty(data.getValue().getMarketCap()).asObject());
        last7dCol.setCellValueFactory(new PropertyValueFactory<>("sparklineIn7d"));
        // Custom cell factory to display the LineChart in the cell

        last7dCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Sparkline sparklineData, boolean empty) {
                super.updateItem(sparklineData, empty);

                if (empty || sparklineData == null) {
                    setGraphic(null);
                } else {
                    LineChart<String, Number> lineChart = createLineChart(sparklineData.getPrice());
                    setGraphic(lineChart);
                }
            }
        });
        // Utilise setItems sur l'instance existante
        tableView.setItems(cryptoData);
    }

    @FXML
    protected void onAddMoneyButtonClick() throws IOException {
        // Création du dialog
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Ajouter de l'argent");

        // Boutons
        ButtonType validerButtonType = new ButtonType("Valider");
        dialog.getDialogPane().getButtonTypes().addAll(validerButtonType, ButtonType.CANCEL);

        // Création du GridPane et mise en place des champs de formulaire
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField amountField = new TextField();
        amountField.setPromptText("Prix");

        grid.add(amountField, 0, 0);

        dialog.getDialogPane().setContent(grid);

        // Convertit le résultat en pair <amountField, buttonType> quand le bouton valider est cliqué.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == validerButtonType) {
                return new Pair<>(amountField.getText(), "Valider");
            }
            return null;
        });

        // Affichage du dialog et attente de l'action de l'utilisateur
        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(amountButtonPair -> {
            System.out.println("Montant ajouté: " + amountButtonPair.getKey());
            // Ici, vous pouvez appeler votre logique pour traiter le montant
        });
    }

    private LineChart<String, Number> createLineChart(List<Double> sparklineData) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setPrefHeight(200);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (int i = 0; i < sparklineData.size(); i++) {
            series.getData().add(new XYChart.Data<>(String.valueOf(i), sparklineData.get(i)));
        }

        lineChart.getData().add(series);

        return lineChart;
    }

    // Méthode pour appeler l'API et récupérer les données
    private List<CryptoCurrency> fetchDataFromApi() {
        ApiCaller apiCaller = new ApiCaller();
        return apiCaller.getAllCoinsMarket("usd", 10, "market_cap_desc", 1, true, "7d", "en");
    }

    private News fetchNewsFromApi() {
        ApiCaller apiCaller = new ApiCaller();
        return apiCaller.getLatestNews();
    }

}
