package com.example.wallet.controller.home;

import com.example.wallet.entity.Articles;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;

public class NewsDisplay {

    public void setupNewsListView(ListView<Articles> newsListView, List<Articles> articles) {
        ObservableList<Articles> articlesList = FXCollections.observableArrayList(articles);
        newsListView.setItems(articlesList);
        // Ajout de l'écouteur de clics
        newsListView.setOnMouseClicked(event -> {
            Articles article = (Articles) newsListView.getSelectionModel().getSelectedItem();
            if (article != null && event.getClickCount() == 1) {
                showArticlePopup(article);
            }
        });
        newsListView.refresh(); // Rafraîchir la ListView après avoir défini les éléments

        newsListView.setCellFactory(param -> new ListCell<Articles>() {
            private ImageView imageView = new ImageView();
            private VBox vbox = new VBox();
            private Text titleLabel = new Text();
            private Text contentLabel = new Text();
            private HBox hBox = new HBox(10); // 10px spacing between elements

            {
                vbox.getChildren().addAll(titleLabel, contentLabel);
                hBox.getChildren().addAll(imageView, vbox);
                hBox.setPadding(new Insets(10, 5, 10, 5));
                contentLabel.setWrappingWidth(250); // Adjust as necessary
            }

            @Override
            protected void updateItem(Articles item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    titleLabel.setText(item.getTitle());
                    contentLabel.setText(item.getBody());

                    if (item.getImageurl() != null && !item.getImageurl().isEmpty()) {
                        Image image = new Image(item.getImageurl(), true); // true to load in background
                        imageView.setImage(image);
                        imageView.setPreserveRatio(true);
                        imageView.setFitHeight(50); // Adjust as necessary
                    }

                    setGraphic(hBox);
                }
            }
        });
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
