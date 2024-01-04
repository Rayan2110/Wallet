package com.example.wallet.controller.home;

import com.example.wallet.ApiCaller;
import com.example.wallet.controller.GestionTransaction;
import com.example.wallet.controller.GestionUser;
import com.example.wallet.controller.GestionWallet;
import com.example.wallet.controller.TransactionType;
import com.example.wallet.entity.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.layout.VBox;
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
    private Label descriptionWallet;

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
    private TableColumn<CryptoCurrency, Void> actionCol;

    private User currentUser;

    @FXML
    private ListView newsListView;

    private NewsDisplay newsDisplay;

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
        newsDisplay = new NewsDisplay();

        News cryptoArticlesApi = fetchNewsFromApi(); // Votre méthode pour récupérer les articles
        newsDisplay.setupNewsListView(newsListView, cryptoArticlesApi.getData());

        currentUser = GestionUser.getInstance().getCurrentUser();
        // Get All Wallets
        if (currentUser != null && currentUser.getWallets() != null && currentUser.getWallets().size() > 0) {
            System.out.println("Nombre de wallets : " + currentUser.getWallets().size());
            for (Wallet wallet : currentUser.getWallets()) {
                MenuItem menuItem = new MenuItem();
                menuItem.setId(String.valueOf(wallet.getId()));
                menuItem.setText(wallet.getTitle());
                menuItem.setOnAction(e -> {
                    switchWallet(wallet);
                });
                walletsItems.getItems().add(menuItem);
            }
            titleWallet.setText(currentUser.getWallets().get(0).getTitle());
            moneyWallet.setText(currentUser.getWallets().get(0).getMoney() + " " + currentUser.getWallets().get(0).getCurrency());
            descriptionWallet.setText(currentUser.getWallets().get(0).getDescription());
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
        actionCol.setCellFactory(col -> {
            Button buyButton = new Button("Acheter");
            TableCell<CryptoCurrency, Void> cell = new TableCell<>() {
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(buyButton);
                        buyButton.setOnAction(event -> {
                            CryptoCurrency crypto = getTableView().getItems().get(getIndex());
                            // Mettez ici la logique pour traiter l'achat
                            buyCryptoCurrency(crypto);
                        });
                    }
                }
            };
            return cell;
        });

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

    private void buyCryptoCurrency(CryptoCurrency crypto) {
        System.out.println(crypto);
        // calcul
        PurchaseTokenPopup popup = new PurchaseTokenPopup();
        double result = popup.showAndWait();
        System.out.println("Nombre entré: " + result);
        // Utilisez 'result' comme nécessaire

    }

    private void switchWallet(Wallet wallet) {
        titleWallet.setText(wallet.getTitle());
        moneyWallet.setText(wallet.getMoney() + " " + wallet.getCurrency());
        descriptionWallet.setText(currentUser.getWallets().get(0).getDescription());
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
        menuItem.setOnAction(e -> switchWallet(wallet));
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
}
