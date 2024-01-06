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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

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
    private PieChart pieChart;

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

    private float moneyLeft;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        newsDisplay = new NewsDisplay();

        News cryptoArticlesApi = fetchNewsFromApi(); // Votre méthode pour récupérer les articles
        newsDisplay.setupNewsListView(newsListView, cryptoArticlesApi.getData());

        currentUser = GestionUser.getInstance().getCurrentUser();
        currentWallet = currentUser.getWallets().get(0);
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
            titleWallet.setText(currentWallet.getTitle());
            moneyWallet.setText(currentWallet.getMoney() + " " + currentWallet.getCurrency());
            descriptionWallet.setText(currentWallet.getDescription());
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
                            buyCryptoCurrency(crypto, currentUser, currentWallet);
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
        updatePieChart();
    }

    private void updatePieChart() {
        PieChart.Data emptyData = new PieChart.Data("", 0.001);

        pieChart.getData().add(emptyData);

        float totalMoney = 0;
        float totalTransaction = 0;
        for (Transaction transaction : currentWallet.getTransactions()) {
            if ("CREATE_WALLET".equals(transaction.getTransactionType())) {
                totalMoney += transaction.getPrice();
            }
            if ("CLONE_WALLET".equals(transaction.getTransactionType())) {
                totalMoney += transaction.getPrice();
            }
            if ("DEPOSIT_MONEY".equals(transaction.getTransactionType())) {
                totalMoney += transaction.getPrice();
            }
            if ("PURCHASE_TOKEN".equals(transaction.getTransactionType())) {
                String title = transaction.getToken();
                PieChart.Data segment = new PieChart.Data(transaction.getToken(), (transaction.getPrice() * 100) / totalMoney);
                totalTransaction += transaction.getPrice();
                pieChart.getData().add(segment);
                addTooltipToChartData(segment, title, transaction.getToken() + " : " + transaction.getPrice());
            }
            if ("SAVING_MONEY".equals(transaction.getTransactionType())) {
                String title = transaction.getToken();
                PieChart.Data segment = new PieChart.Data(transaction.getToken(), (transaction.getPrice() * 100) / totalMoney);
                totalTransaction += transaction.getPrice();
                pieChart.getData().add(segment);
                addTooltipToChartData(segment, title, "Épargne : " + transaction.getPrice());
            }
        }
        moneyWallet.setText(totalMoney + " " + currentWallet.getCurrency());
        PieChart.Data segment = new PieChart.Data("Argent encore disponible", (totalTransaction * 100) / totalMoney);
        pieChart.getData().add(segment);
        moneyLeft = totalMoney - totalTransaction;
        addTooltipToChartData(segment, String.valueOf(((totalMoney-totalTransaction) * 100) / totalMoney), "Argent encore disponible : " + moneyLeft);

        // Autres configurations
        pieChart.setLabelsVisible(false);
    }


    private void addTooltipToChartData(PieChart.Data data, String title, String tooltipText) {
        Tooltip tooltip = new Tooltip(tooltipText);
        data.setName(title);  // Ajouter un titre au segment
        data.getNode().addEventHandler(MouseEvent.MOUSE_MOVED, e -> {
            tooltip.show(data.getNode(), e.getScreenX(), e.getScreenY() + 15);
        });
        data.getNode().addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            tooltip.hide();
        });
    }

    @FXML

    private void onHandleEpargneButton() {
        showEpargneDialog();
    }

    private void showEpargneDialog() {
        // Créer un nouveau dialogue
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Épargne");
        dialog.setHeaderText("Entrez le montant à épargner");

        // Ajouter un bouton de type OK et Cancel
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Créer un champ de texte pour l'entrée
        TextField inputField = new TextField();
        inputField.setPromptText("Montant");

        // Ajouter le champ de texte au dialogue
        dialog.getDialogPane().setContent(inputField);

        // Convertir le résultat en une chaîne lors de l'appui sur OK
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return inputField.getText();
            }
            return null;
        });

        // Afficher le dialogue et attendre la réponse de l'utilisateur
        Optional<String> result = dialog.showAndWait();

        // Traiter la réponse
        result.ifPresent(montant -> {
            System.out.println("Montant épargné: " + montant);
            // Ajoutez ici le code pour gérer l'épargne
            saveToTransactionFile(montant);
        });

    }
    private void saveToTransactionFile(String montant) {
            Transaction transaction = new Transaction(TransactionType.SAVING_MONEY.name(), Float.parseFloat(montant), LocalDateTime.now(), 0, null);
            GestionTransaction gestionTransaction = new GestionTransaction();
            gestionTransaction.writeTransaction(transaction, currentWallet.getId(), currentUser.getId());
            currentWallet.getTransactions().add(transaction);
            updatePieChart();
            transactionData.clear();
            List<Transaction> transactions = currentWallet.getTransactions();
            transactionData.addAll(transactions); // Ajouter les transactions à la liste observable
            transactionsTableView.setItems(transactionData);
            transactionsTableView.setItems(transactionData);
            transactionsTableView.refresh();
    }


    private void buyCryptoCurrency(CryptoCurrency crypto, User currentUser, Wallet currentWallet) {
        System.out.println(crypto);
        // calcul
        PurchaseTokenPopup popup = new PurchaseTokenPopup(crypto, currentWallet, currentUser.getId(), transactionData);
        float result = popup.showAndWait();

        System.out.println("Nombre entré: " + result);

        transactionsTableView.refresh();
        // Utilisez 'result' comme nécessaire

    }

    private void switchWallet(Wallet wallet) {
        currentWallet = wallet;
        titleWallet.setText(currentWallet.getTitle());
        moneyWallet.setText(currentWallet.getMoney() + " " + currentWallet.getCurrency());
        descriptionWallet.setText(currentWallet.getDescription());
        updateTableTransaction(wallet);
    }

    private void updateTableTransaction(Wallet wallet) {
        transactionData.clear();
        List<Transaction> transactions = wallet.getTransactions();
        transactionData.addAll(transactions);
        transactionsTableView.setItems(transactionData);

        transactionsTableView.setItems(transactionData);

        transactionsTableView.refresh();
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
        return apiCaller.getAllCoinsMarket("eur", 10, "market_cap_desc", 1, true, "7d", "fr");
    }

    private News fetchNewsFromApi() {
        ApiCaller apiCaller = new ApiCaller();
        return apiCaller.getLatestNews();
    }

    private Optional<Float> afficherDialogueDepot() {
        TextInputDialog dialog = new TextInputDialog("0");
        dialog.setTitle("Dépôt d'Argent");
        dialog.setHeaderText("Déposer de l'argent");
        dialog.setContentText("Veuillez entrer le montant du dépôt :");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                return Optional.of(Float.parseFloat(result.get()));
            } catch (NumberFormatException e) {
                // Gérer l'erreur si l'entrée n'est pas un nombre valide
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }

    }

    @FXML
    public void onHandleDepotButton(ActionEvent actionEvent) {
        Optional<Float> montantDepot = afficherDialogueDepot();
        montantDepot.ifPresent(montant -> {
            Transaction transaction = new Transaction(TransactionType.DEPOSIT_MONEY.name(), montant, LocalDateTime.now(), 0, null);
            GestionTransaction gestionTransaction = new GestionTransaction();
            gestionTransaction.writeTransaction(transaction, currentWallet.getId(), currentUser.getId());
            currentWallet.getTransactions().add(transaction);
            transactionData.add(transaction);
            transactionsTableView.refresh();
            currentWallet.setMoney(currentWallet.getMoney() + montant);
            moneyWallet.setText(currentWallet.getMoney() + " " + currentWallet.getCurrency());
        });

    }

    public void onHandleCloneButton(ActionEvent actionEvent) {
        Optional<Pair<String, String>> result = afficherDialogueClonage();

        result.ifPresent(titreDescription -> {
            String nouveauTitre = titreDescription.getKey();
            String nouvelleDescription = titreDescription.getValue();

            GestionWallet gestionWallet = new GestionWallet();
            gestionWallet.newWallet(currentWallet, currentUser.getId());

            Wallet wallet = new Wallet(nouveauTitre, nouvelleDescription, currentWallet.getMoney(), LocalDateTime.now(), currentWallet.getCurrency());
            Transaction transaction = new Transaction(TransactionType.CLONE_WALLET.name(), currentWallet.getMoney(), LocalDateTime.now(), 0, null);

            GestionTransaction gestionTransaction = new GestionTransaction();
            gestionWallet.newWallet(wallet, currentUser.getId());
            gestionTransaction.writeTransaction(transaction, wallet.getId(), currentUser.getId());

            wallet.getTransactions().add(transaction);
            currentUser.getWallets().add(wallet);

            MenuItem menuItem = new MenuItem(nouveauTitre);
            menuItem.setOnAction(e -> switchWallet(wallet));
            walletsItems.getItems().add(menuItem);
        });
    }


    private Optional<Pair<String, String>> afficherDialogueClonage() {
        // Création de la boîte de dialogue
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Cloner le Portefeuille");
        dialog.setHeaderText("Entrez le titre et la description du nouveau portefeuille");

        // Boutons
        ButtonType btnCloner = new ButtonType("Cloner", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnCloner, ButtonType.CANCEL);

        // Création des champs de saisie
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titre = new TextField();
        titre.setPromptText("Titre");
        TextField description = new TextField();
        description.setPromptText("Description");

        grid.add(new Label("Titre:"), 0, 0);
        grid.add(titre, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(description, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Convertit le résultat en paires titre-description lorsque le bouton Cloner est cliqué
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnCloner) {
                return new Pair<>(titre.getText(), description.getText());
            }
            return null;
        });

        return dialog.showAndWait();
    }
    public static class Triplet<T, U, V> {
        private final T first;
        private final U second;
        private final V third;

        public Triplet(T first, U second, V third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }

        public T getFirst() {
            return first;
        }

        public U getSecond() {
            return second;
        }

        public V getThird() {
            return third;
        }
    }

    public class TransactionData {

        public static Set<String> readTransactions(String fieldName) {
            Set<String> data = new HashSet<>();
            try {
                File file = new File("src/main/resources/bdd/transactions.txt");
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    System.out.println("Ligne lue: " + line);
                    // Vérifie si la ligne contient "PURCHASE_TOKEN"
                    if (line.contains("PURCHASE_TOKEN")) {
                        System.out.println("Ligne correspondante trouvée: " + line); // Pour le débogage
                    }
                    String[] parts = line.split("|");// Assurez-vous que ce séparateur correspond à votre fichier


                    if ("token".equals(fieldName) && parts.length > 0) {
                        data.add(parts[0]); // token
                    } else if ("amount".equals(fieldName) && parts.length > 1) {
                        data.add(parts[1]); // amount
                    } else if ("price".equals(fieldName) && parts.length > 2) {
                        data.add(parts[2]); // price
                    }
                }
                scanner.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return data;
        }
    }


    @FXML
    public Optional<Triplet<String, String, String>> onHandleSellButton(ActionEvent actionEvent) {
            // Création de la boîte de dialogue
            Dialog<Triplet<String, String, String>> dialog = new Dialog<>();
            dialog.setTitle("Détails de la Transaction");
            dialog.setHeaderText("Sélectionnez les détails de la transaction");

            // Boutons
            ButtonType btnValider = new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(btnValider, ButtonType.CANCEL);

            // Création des ComboBox
            ComboBox<String> comboCryptoMonnaie = new ComboBox<>();
            comboCryptoMonnaie.getItems().addAll(TransactionData.readTransactions("token"));

            ComboBox<String> comboValeurToken = new ComboBox<>();
            comboValeurToken.getItems().addAll(TransactionData.readTransactions("amount"));

            ComboBox<String> comboValeurEurosDollars = new ComboBox<>();
            comboValeurEurosDollars.getItems().addAll(TransactionData.readTransactions("price"));

            // Ajout des ComboBox au GridPane
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            grid.add(new Label("Crypto-monnaie:"), 0, 0);
            grid.add(comboCryptoMonnaie, 1, 0);
            grid.add(new Label("Valeur du token:"), 0, 1);
            grid.add(comboValeurToken, 1, 1);
            grid.add(new Label("Valeur en euros/dollars:"), 0, 2);
            grid.add(comboValeurEurosDollars, 1, 2);

            dialog.getDialogPane().setContent(grid);

            // Convertit le résultat en un triplet lorsque le bouton Valider est cliqué
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == btnValider) {
                    return new Triplet<>(
                            comboCryptoMonnaie.getSelectionModel().getSelectedItem(),
                            comboValeurToken.getSelectionModel().getSelectedItem(),
                            comboValeurEurosDollars.getSelectionModel().getSelectedItem()
                    );
                }
                return null;
            });

            // Affiche la boîte de dialogue et attend une réponse
            return dialog.showAndWait();
        }
}
