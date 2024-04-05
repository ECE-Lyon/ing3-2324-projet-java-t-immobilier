module com.example.demo2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.demo2 to javafx.fxml;
    exports com.example.demo2;
    exports com.myapp.ui;
    opens com.myapp.ui to javafx.fxml;
}