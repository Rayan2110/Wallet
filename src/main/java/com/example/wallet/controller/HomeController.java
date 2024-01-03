package com.example.wallet.controller;

import com.example.wallet.ApiCaller;
import com.example.wallet.entity.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private Label titleWallet;

    @FXML
    private Label moneyWallet;

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

    private User currentUser;

    @FXML
    private ListView newsListView;

    @FXML
    private Menu walletsItems;

    @FXML
    private TableView<Transaction> transactionsTableView;
    @FXML
    private TableColumn<Transaction, Number> priceField;
    @FXML
    private TableColumn<Transaction, String> dateField;
    @FXML
    private TableColumn<Transaction, String> typeField;
    @FXML
    private TableColumn<Transaction, Number> amountField;
    @FXML
    private TableColumn<Transaction, String> tokenField;

    private ObservableList<Transaction> transactionData = FXCollections.observableArrayList();

    private Wallet currentWallet;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentUser = GestionUser.getInstance().getCurrentUser();
        // Get All Wallets
        if (currentUser != null && currentUser.getWallets() != null && currentUser.getWallets().size() > 0) {
            System.out.println("Nombre de wallets : " + currentUser.getWallets().size());
            for (Wallet wallet : currentUser.getWallets()) {
                MenuItem menuItem = new MenuItem();
                menuItem.setId(String.valueOf(wallet.getId()));
                menuItem.setText(wallet.getTitle());
                menuItem.setOnAction(e -> switchWallet(e.getTarget().toString()));
                walletsItems.getItems().add(menuItem);
            }
            titleWallet.setText(currentUser.getWallets().get(0).getTitle());
            moneyWallet.setText(currentUser.getWallets().get(0).getMoney() + " " + currentUser.getWallets().get(0).getCurrency());
        }
        dateField.setCellValueFactory(new PropertyValueFactory<>("date"));
        typeField.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        amountField.setCellValueFactory(new PropertyValueFactory<>("amount"));
        tokenField.setCellValueFactory(new PropertyValueFactory<>("token"));
        priceField.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Chargez vos transactions depuis le fichier ici
        List<Transaction> transactions = currentUser.getWallets().get(0).getTransactions();
        transactionData.addAll(transactions); // Ajouter les transactions à la liste observable
        transactionsTableView.setItems(transactionData);

        transactionsTableView.setItems(transactionData);

        transactionsTableView.refresh();


        List<CryptoCurrency> cryptoDataApi = fetchDataFromApi();
        News cryptoArticlesApi = fetchNewsFromApi();
        displayNews(cryptoArticlesApi);


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

        newsListView.setCellFactory(param -> new ListCell<Articles>() {
            private ImageView imageView = new ImageView();
            private VBox vbox = new VBox();
            private Label titleLabel = new Label();
            private Label contentLabel = new Label();
            private HBox hBox = new HBox(10); // 10px spacing between elements

            {
                vbox.getChildren().addAll(titleLabel, contentLabel);
                hBox.getChildren().addAll(imageView, vbox);
                hBox.setPadding(new Insets(10, 5, 10, 5)); // Padding around the HBox
                contentLabel.setWrapText(true);
            }

            @Override
            protected void updateItem(Articles item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    titleLabel.setText(item.getTitle());
                    contentLabel.setText(item.getBody());

                    // Assuming NewsItem has a getImageUrl method that returns a String
                    if (item.getImageurl() != null && !item.getImageurl().isEmpty()) {
                        Image image = new Image(item.getImageurl(), true); // true to load in background
                        imageView.setImage(image);
                        imageView.setPreserveRatio(true);
                        imageView.setFitHeight(50); // or the size you want
                    }

                    setGraphic(hBox);
                }
            }
        });
    }

    private void switchWallet(String wallet) {
        System.out.println(wallet);
    }

    @FXML
    protected void onAddMoneyButtonClick() throws IOException {
        Stage popupWindow = new Stage();
        popupWindow.initModality(Modality.APPLICATION_MODAL);
        popupWindow.setTitle("Formulaire");

        // Création des éléments du formulaire
        Label labelWalletTitle = new Label("Entrez le nom de votre porte-monnaie");
        TextField textFieldWalletTitle = new TextField();

        Label labelWalletDescription = new Label("Entrez une description");
        TextField textFieldWalletDescription = new TextField();

        Label labelWalletMoney = new Label("Montant à ajouter");
        TextField numberField = new TextField();
        numberField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("[0-9]")) {
                event.consume();
            }
        });

        // ComboBox pour la devise
        ComboBox<String> currencyComboBox = new ComboBox<>();
        currencyComboBox.getItems().addAll("EUR", "USD");
        currencyComboBox.setValue("EUR"); // Valeur par défaut

        Button submitButton = new Button("Soumettre");
        submitButton.setOnAction(e -> handleSubmitButton(textFieldWalletTitle.getText(), textFieldWalletDescription.getText(), numberField.getText(), currencyComboBox.getValue(), popupWindow));

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(labelWalletTitle, textFieldWalletTitle, labelWalletDescription, textFieldWalletDescription, labelWalletMoney, numberField, currencyComboBox, submitButton);

        // Paramétrage de la scène et affichage
        Scene scene = new Scene(layout, 1024, 768);
        popupWindow.setScene(scene);
        popupWindow.showAndWait();
    }


    private void handleSubmitButton(String title, String description, String amount, String currency, Stage popupStage) {
        // Traitez ici les données saisies
        System.out.println("Title : " + title + ", Description : " + description);
        System.out.println("Montant : " + amount + ", Devise : " + currency);
        Wallet wallet = new Wallet(title, description, Float.parseFloat(amount), LocalDateTime.now(), currency);
        GestionWallet gestionWallet = new GestionWallet();
        Transaction transaction = new Transaction(TransactionType.CREATE_WALLET.name(), Float.parseFloat(amount), LocalDateTime.now(), 0, null);
        GestionTransaction gestionTransaction = new GestionTransaction();
        gestionWallet.newWallet(wallet, currentUser.getId());
        gestionTransaction.writeTransaction(transaction, wallet.getId(), currentUser.getId());
        wallet.getTransactions().add(transaction);
        currentUser.getWallets().add(wallet);
        MenuItem menuItem = new MenuItem(title);
        menuItem.setOnAction(e->switchWallet(e.getTarget().toString()));
        walletsItems.getItems().add(menuItem);
        popupStage.close();

        // Afficher un message de succès
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Succès");
        successAlert.setHeaderText("Opération réussie avec le montant : " + amount + " " + currency);
        successAlert.showAndWait();
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

    private void displayNews(News cryptoArticlesApi) {
        ObservableList<Articles> articlesList = FXCollections.observableArrayList(cryptoArticlesApi.getData());
        newsListView.setItems(articlesList);
        // Ajout de l'écouteur de clics
        newsListView.setOnMouseClicked(event -> {
            Articles article = (Articles) newsListView.getSelectionModel().getSelectedItem();
            if (article != null && event.getClickCount() == 1) {
                showArticlePopup(article);
            }
        });
        newsListView.refresh(); // Rafraîchir la ListView après avoir défini les éléments
    }

    private void showArticlePopup(Articles article) {
        // Création du dialog
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle(article.getTitle());

        // Création du GridPane et mise en place des détails de l'article
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ImageView imageView = new ImageView();
        if (article.getImageurl() != null && !article.getImageurl().isEmpty()) {
            Image image = new Image(article.getImageurl(), true); // true to load in background
            imageView.setImage(image);
            imageView.setPreserveRatio(true);
            imageView.setFitHeight(200); // Adjust as necessary
        }

        Text contentText = new Text(article.getBody());
        contentText.setWrappingWidth(300); // Adjust as necessary

        grid.add(imageView, 0, 0);
        grid.add(contentText, 0, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        dialog.showAndWait();
    }


}
