module com.example.wallet {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.wallet to javafx.fxml;
    exports com.example.wallet;
    exports com.example.wallet.controller;
    opens com.example.wallet.controller to javafx.fxml;
}