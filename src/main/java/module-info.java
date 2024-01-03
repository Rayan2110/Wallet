module com.example.wallet {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens com.example.wallet.entity; // Ajoutez cette ligne
    opens com.example.wallet to javafx.fxml;
    exports com.example.wallet.entity;
    exports com.example.wallet;
    exports com.example.wallet.controller;
    opens com.example.wallet.controller to javafx.fxml;
    exports com.example.wallet.controller.home;
    opens com.example.wallet.controller.home to javafx.fxml;

}