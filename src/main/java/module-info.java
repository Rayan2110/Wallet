module com.example.wallet {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.wallet to javafx.fxml;
    exports com.example.wallet;
}