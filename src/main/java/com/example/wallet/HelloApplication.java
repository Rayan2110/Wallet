package com.example.wallet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    public static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768); // standard de base

        //FXMLLoader fxmlLoader2 = new FXMLLoader(HelloApplication.class.getResource("connexion-view.fxml"));
        //Scene scene2 = new Scene(fxmlLoader2.load(), 640 ,240);

        stage.setTitle("Financial Wallet");
        stage.setScene(scene);
        HelloApplication.stage = stage;
        stage.show();


         /*       Button b = (Button) scene.lookup("#connexion");
                b.setOnAction(e->{
                    stage.setScene(scene2);

                });

        VBox vb2 = (VBox) scene2.lookup("#vbox2");
        Button b2 = new Button("Get Back");
        b2.setOnMouseClicked( e -> {
                stage.setScene(scene);
        });
        vb2.getChildren().add(b2);

          */
    }

    public static void main(String[] args) {
        launch();
    }
}