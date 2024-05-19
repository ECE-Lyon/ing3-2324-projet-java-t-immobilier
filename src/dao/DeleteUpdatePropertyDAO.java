package dao;

import models.Address;
import models.Property;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeleteUpdatePropertyDAO {
    Connection connection = null;

    public static List<Property> getAllProperties() {
        List<Property> properties = new ArrayList<>();
        String sql = "SELECT id_property, size, description, price, status_sold, has_pool, has_garden, property_type, nb_room, program_visit, id_client, id_employee FROM property";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                properties.add(new Property(
                        rs.getInt("id_property"),
                        rs.getFloat("size"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getBoolean("status_sold"),
                        rs.getBoolean("has_pool"),
                        rs.getBoolean("has_garden"),
                        rs.getString("property_type"),
                        rs.getInt("nb_room"),
                        rs.getBoolean("program_visit"),
                        rs.getInt("id_client"),
                        rs.getInt("id_employee")
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

    public static void updateProperty(Property property) {
        String query = "UPDATE PROPERTY SET size = ?, description = ?, price = ?, status_sold = ?, has_pool = ?, has_garden = ?, property_type = ?, nb_room = ? WHERE id_property = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setDouble(1, property.getSize());
            statement.setString(2, property.getDescription());
            statement.setDouble(3, property.getPrice());
            statement.setBoolean(4, property.isStatusSold());
            statement.setBoolean(5, property.isHasPool());
            statement.setBoolean(6, property.isHasGarden());
            statement.setString(7, property.getPropertyType());
            statement.setInt(8, property.getNbRoom());
            statement.setInt(9, property.getIdProperty());
            statement.executeUpdate();
            System.out.println("Propriété modifiée");
        } catch (SQLException e) {
            System.out.println("Error updating property: " + e.getMessage());
        }
    }

    public static void deleteProperty(int id) {
        String deleteAddressQuery = "DELETE FROM ADDRESS WHERE id_property = ?";
        String deletePropertyQuery = "DELETE FROM PROPERTY WHERE id_property = ?";

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Désactiver les contraintes de clé étrangère
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("SET FOREIGN_KEY_CHECKS=0");
            }

            // Supprimer les adresses associées à la propriété
            try (PreparedStatement addressStmt = connection.prepareStatement(deleteAddressQuery)) {
                addressStmt.setInt(1, id);
                addressStmt.executeUpdate();
            }

            // Supprimer la propriété
            try (PreparedStatement propertyStmt = connection.prepareStatement(deletePropertyQuery)) {
                propertyStmt.setInt(1, id);
                propertyStmt.executeUpdate();
            }

            // Réactiver les contraintes de clé étrangère
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("SET FOREIGN_KEY_CHECKS=1");
            }

        } catch (SQLException e) {
            System.out.println("Impossible de supprimer la propriété: " + e.getMessage());
        }
    }
}
