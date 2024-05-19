module com.example.demo2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.management;


    opens com.example.demo2 to javafx.fxml;
    exports com.example.demo2;
    opens dao to javafx.fxml;
    exports views;
    opens views to javafx.fxml;
    exports models;
    opens models to javafx.fxml;
}