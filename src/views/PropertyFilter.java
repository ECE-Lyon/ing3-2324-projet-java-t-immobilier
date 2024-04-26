package views;

import dao.DatabaseConnection;
import dao.PropertyDAO;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PropertyFilter extends Application {
    private ComboBox<String> cityComboBox;
    private ComboBox<String> typeComboBox;
    private TextField minPriceField;
    private TextField maxPriceField;
    private final PropertyDAO propertyDAO = new PropertyDAO();
    private VBox propertyInfoContainer;

    @Override
    public void start(Stage primaryStage) {
        boolean i = false;
        VBox root = new VBox();
        root.setSpacing(10);
        root.setPadding(new Insets(10));

        Label titleLabel = new Label("Properties Information");

        cityComboBox = new ComboBox<>();
        ObservableList<String> cityOptions = FXCollections.observableArrayList("Toute"); // Ajout de "Toute" comme première option
        cityOptions.addAll(propertyDAO.getDistinctValues("city", "ADDRESS"));
        cityComboBox.setItems(cityOptions);

        typeComboBox = new ComboBox<>();
        ObservableList<String> typeOptions = FXCollections.observableArrayList("Toute"); // Ajout de "Toute" comme première option
        typeOptions.addAll(propertyDAO.getDistinctValues("property_type", "PROPERTY"));
        typeComboBox.setItems(typeOptions);

        minPriceField = new TextField();
        minPriceField.setPromptText("Prix minimum");

        maxPriceField = new TextField();
        maxPriceField.setPromptText("Prix maximum");

        Button filterButton = new Button("Filtrer");
        filterButton.setOnAction(event -> filterProperties(primaryStage));

        Button resetButton = new Button("Réinitialiser les filtres");
        resetButton.setOnAction(event -> resetFilters(i));

        propertyInfoContainer = new VBox();
        propertyInfoContainer.setSpacing(5);

        // Affichage de toutes les propriétés disponibles au démarrage de l'application
        displayAllProperties(propertyInfoContainer);

        root.getChildren().addAll(titleLabel, cityComboBox, typeComboBox, minPriceField, maxPriceField, filterButton, resetButton, propertyInfoContainer);

        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Property Information");
        primaryStage.show();
    }

    private void displayAllProperties(VBox propertyInfoBox) {
        StringBuilder allPropertiesInfo = new StringBuilder();

        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT p.id_property, p.size, p.description, p.price, p.property_type, a.city, a.postal_code, a.number_street, a.name_street " +
                    "FROM PROPERTY p " +
                    "JOIN ADDRESS a ON p.id_property = a.id_property");

            while (resultSet.next()) {
                int id = resultSet.getInt("id_property");
                float size = resultSet.getFloat("size");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                String type = resultSet.getString("property_type");
                String city = resultSet.getString("city");
                String postalCode = resultSet.getString("postal_code");
                int numberStreet = resultSet.getInt("number_street");
                String nameStreet = resultSet.getString("name_street");

                allPropertiesInfo.append("Property ID: ").append(id).append("\n")
                        .append("Size: ").append(size).append("\n")
                        .append("Description: ").append(description).append("\n")
                        .append("Price: ").append(price).append("\n")
                        .append("Type: ").append(type).append("\n")
                        .append("City: ").append(city).append("\n")
                        .append("Address: ").append(numberStreet).append(" ").append(nameStreet).append(", ").append(postalCode).append("\n")
                        .append("-----------------------------------------").append("\n");
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        propertyInfoBox.getChildren().add(new Label(allPropertiesInfo.toString()));
    }


    private void filterProperties(Stage primaryStage) {
        String cityName = cityComboBox.getValue();
        String typeName = typeComboBox.getValue();
        String minPriceStr = minPriceField.getText();
        String maxPriceStr = maxPriceField.getText();
        StringBuilder propertyInfo = new StringBuilder();

        String sqlQuery = "SELECT p.id_property, p.size, p.description, p.price, p.property_type, a.city, a.postal_code, a.number_street, a.name_street " +
                "FROM PROPERTY p " +
                "JOIN ADDRESS a ON p.id_property = a.id_property " +
                "WHERE ";

        if (cityName != null && !cityName.equals("Toute")) {
            sqlQuery += "a.city = '" + cityName + "'";
        } else {
            sqlQuery += "1=1"; // Condition toujours vraie pour ne pas briser la requête SQL
        }

        if (typeName != null && !typeName.equals("Toute")) {
            sqlQuery += " AND p.property_type = '" + typeName + "'";
        }

        if (!minPriceStr.isEmpty()) {
            double minPrice = Double.parseDouble(minPriceStr);
            sqlQuery += " AND p.price >= " + minPrice;
        }

        if (!maxPriceStr.isEmpty()) {
            double maxPrice = Double.parseDouble(maxPriceStr);
            sqlQuery += " AND p.price <= " + maxPrice;
        }

        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);

            while (resultSet.next()) {
                int id = resultSet.getInt("id_property");
                float size = resultSet.getFloat("size");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                String type = resultSet.getString("property_type");
                String city = resultSet.getString("city");
                String postalCode = resultSet.getString("postal_code");
                int numberStreet = resultSet.getInt("number_street");
                String nameStreet = resultSet.getString("name_street");

                propertyInfo.append("Property ID: ").append(id).append("\n")
                        .append("Size: ").append(size).append("\n")
                        .append("Description: ").append(description).append("\n")
                        .append("Price: ").append(price).append("\n")
                        .append("Type: ").append(type).append("\n")
                        .append("City: ").append(city).append("\n")
                        .append("Address: ").append(numberStreet).append(" ").append(nameStreet).append(", ").append(postalCode).append("\n")
                        .append("-----------------------------------------").append("\n");
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        propertyInfoContainer.getChildren().clear();
        propertyInfoContainer.getChildren().add(new Label(propertyInfo.toString()));
    }

    private boolean resetFilters(boolean i) {
        if (!i){
            cityComboBox.setValue("Toute");
            typeComboBox.setValue("Toute");
            minPriceField.clear();
            maxPriceField.clear();
            displayAllProperties(propertyInfoContainer);
            i=true;
        }else {
            i = false;
        }


        return i;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
