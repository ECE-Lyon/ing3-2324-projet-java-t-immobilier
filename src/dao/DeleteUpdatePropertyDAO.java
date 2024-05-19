package dao;

import javafx.stage.Stage;
import models.Address;
import models.Property;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static javax.management.remote.JMXConnectorFactory.connect;

public class DeleteUpdatePropertyDAO {
    Connection connection = null;

    public static List<Property> getAllProperties() {
        List<Property> properties = new ArrayList<>();
        String sql = "SELECT id, street_number, street_name, postal_code, city, price, size, rooms, garden, pool, type FROM properties";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                properties.add(new Property(
                        rs.getInt("id"),
                        rs.getString("street_number"),
                        rs.getString("street_name"),
                        rs.getString("postal_code"),
                        rs.getString("city"),
                        rs.getDouble("price"),
                        rs.getDouble("size"),
                        rs.getInt("rooms"),
                        rs.getBoolean("garden"),
                        rs.getBoolean("pool"),
                        rs.getString("type")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching properties: " + e.getMessage());
        }
        return properties;
    }

    private static Connection connect() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    public static void updateProperty(Property property, Address address) {
        String query = "UPDATE properties SET street_number = ?, street_name = ?, postal_code = ?, city = ?, price = ?, size = ?, rooms = ?, garden = ?, pool = ?, type = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, address.getStreetNumber());
            statement.setString(2, address.getStreetName());
            statement.setInt(3, address.getPostalCode());
            statement.setString(4, address.getCity());
            statement.setDouble(5, property.getPrice());
            statement.setDouble(6, property.getSize());
            statement.setInt(7, property.getNumberOfRooms());
            statement.setBoolean(8, property.isHasGarden());
            statement.setBoolean(9, property.isHasPool());
            statement.setString(10, property.getPropertyType());
            statement.setInt(11, property.getIdProperty());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating property: " + e.getMessage());
        }
    }

    public static void deleteProperty(int id) {
        String query = "DELETE FROM properties WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Impossible de supprimer la propriété: " + e.getMessage());
        }
    }



}
