package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Property;


public class PropertyDAO {
    public List<Property> searchProperties(String searchCity, String propertyType, double budget, double size, boolean hasPool, boolean hasGarden) {
        List<Property> properties = new ArrayList<>();
        String query = "SELECT p.id_property, p.size, p.description, p.price, p.property_type, p.status_sold, p.has_pool, p.has_garden, p.nb_room, p.program_visit, p.id_buyer, p.id_seller, p.id_visit, p.id_employee, a.city " +
                "FROM PROPERTY p " +
                "JOIN ADDRESS a ON p.id_property = a.id_property " +
                "WHERE a.city = ? AND p.property_type = ? AND p.price <= ? AND p.size <= ? AND p.has_pool = ? AND p.has_garden = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, searchCity);
            statement.setString(2, propertyType);
            statement.setDouble(3, budget);
            statement.setDouble(4, size);
            statement.setBoolean(5, hasPool);
            statement.setBoolean(6, hasGarden);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int idProperty = resultSet.getInt("id_property");
                    double propertySize = resultSet.getDouble("size");
                    String description = resultSet.getString("description");
                    double price = resultSet.getDouble("price");
                    boolean statusSold = resultSet.getBoolean("status_sold");
                    boolean propertyHasPool = resultSet.getBoolean("has_pool");
                    boolean propertyHasGarden = resultSet.getBoolean("has_garden");
                    String type = resultSet.getString("property_type");
                    int numberOfRooms = resultSet.getInt("nb_room");
                    boolean programVisit = resultSet.getBoolean("program_visit");
                    int idClient = resultSet.getInt("id_buyer");
                    //int idSeller = resultSet.getInt("id_seller");
                    //int idVisit = resultSet.getInt("id_visit");
                    int idEmployee = resultSet.getInt("id_employee");
                    //String title = resultSet.getString("title");
                    //String imagePath = resultSet.getString("imagePath");
                    //String city = resultSet.getString("city");

                    Property property = new Property(idProperty, propertySize, description, price, statusSold, propertyHasPool, propertyHasGarden, type, numberOfRooms, programVisit, idClient,idEmployee);
                    properties.add(property);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return properties;
    }


    public ObservableList<String> getDistinctValues(String columnName, String tableName) {
        ObservableList<String> distinctValues = FXCollections.observableArrayList();

        String query = "SELECT DISTINCT " + columnName + " FROM " + tableName;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet resultSet = stmt.executeQuery()) {

            while (resultSet.next()) {
                distinctValues.add(resultSet.getString(columnName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return distinctValues;
    }


}
