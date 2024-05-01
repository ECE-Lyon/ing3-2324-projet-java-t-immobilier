package controllers;

import dao.DatabaseConnection;
import dao.PropertyDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PropertyFilterController {
    private ComboBox<String> cityComboBox;
    private ComboBox<String> typeComboBox;
    private TextField minPriceField;
    private TextField maxPriceField;
    private final PropertyDAO propertyDAO = new PropertyDAO();
    private GridPane propertyGrid;

    private void displayProperties(ResultSet resultSet) throws SQLException {
        propertyGrid.getChildren().clear();
        int row = 0;
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

            // Création de l'image statique
            Image image = new Image(getClass().getResourceAsStream("/image1.jpg"));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);

            propertyGrid.addRow(row, new Label("Property ID: " + id), new Label("Size: " + size), new Label("Description: " + description),
                    new Label("Price: " + price), new Label("Type: " + type), new Label("City: " + city),
                    new Label("Address: " + numberStreet + " " + nameStreet + ", " + postalCode), imageView);

            row++;
        }
    }

    private void displayAllProperties() {
        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT p.id_property, p.size, p.description, p.price, p.property_type, a.city, a.postal_code, a.number_street, a.name_street " +
                    "FROM PROPERTY p " +
                    "JOIN ADDRESS a ON p.id_property = a.id_property");
            displayProperties(resultSet);
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initialize() {
        // Initialisation des combobox et des champs de texte
        ObservableList<String> cityOptions = FXCollections.observableArrayList("Toute");
        cityOptions.addAll(propertyDAO.getDistinctValues("city", "ADDRESS"));
        cityComboBox.setItems(cityOptions);
        cityComboBox.setPromptText("Ville");

        ObservableList<String> typeOptions = FXCollections.observableArrayList("Toute");
        typeOptions.addAll(propertyDAO.getDistinctValues("property_type", "PROPERTY"));
        typeComboBox.setItems(typeOptions);
        typeComboBox.setPromptText("Type de propriété");

        // Affichage de toutes les propriétés au démarrage de l'application
        displayAllProperties();
    }

    private void filterProperties() {
        String cityName = cityComboBox.getValue();
        String typeName = typeComboBox.getValue();
        String minPriceStr = minPriceField.getText();
        String maxPriceStr = maxPriceField.getText();

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
            displayProperties(resultSet);
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void resetFilters() {
        cityComboBox.setValue("Toute");
        typeComboBox.setValue("Toute");
        minPriceField.clear();
        maxPriceField.clear();
        displayAllProperties();
    }

}
