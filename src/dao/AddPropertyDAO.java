package dao;

import java.sql.*;
import dao.DatabaseConnection;

public class AddPropertyDAO {

    public boolean addProperty(String number_street, String name_street, String city, String postal_code, double size, String description, double price, int status_sold, boolean has_pool, boolean has_garden, String property_type, int nb_room, int program_visit, int id_client, int id_employee) {
        String insertAddress = "INSERT INTO address (number_street, name_street, city, postal_code) VALUES (?, ?, ?, ?)";
        String insertProperty = "INSERT INTO property (size, description, price, status_sold, has_pool, has_garden, property_type, nb_room, program_visit, id_client, id_employee) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection connection = null;
        PreparedStatement stmtAddress = null;
        PreparedStatement stmtProperty = null;
        ResultSet generatedKeys = null;

        try {
            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false);  // S'assurer que la transaction est gérée manuellement

            // Insertion dans la table address
            stmtAddress = connection.prepareStatement(insertAddress, Statement.RETURN_GENERATED_KEYS);
            stmtAddress.setString(1, number_street);
            stmtAddress.setString(2, name_street);
            stmtAddress.setString(3, city);
            stmtAddress.setString(4, postal_code);
            int addressAffected = stmtAddress.executeUpdate();

            if (addressAffected == 1) {
                generatedKeys = stmtAddress.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int id_property = generatedKeys.getInt(1);

                    // Insertion dans la table property
                    stmtProperty = connection.prepareStatement(insertProperty);
                    stmtProperty.setDouble(1, size);
                    stmtProperty.setString(2, description);
                    stmtProperty.setDouble(3, price);
                    stmtProperty.setInt(4, status_sold);
                    stmtProperty.setBoolean(5, has_pool);
                    stmtProperty.setBoolean(6, has_garden);
                    stmtProperty.setString(7, property_type);
                    stmtProperty.setInt(8, nb_room);
                    stmtProperty.setInt(9, program_visit);
                    stmtProperty.setInt(10, id_client);
                    stmtProperty.setInt(11, id_employee);

                    int propertyAffected = stmtProperty.executeUpdate();
                    if (propertyAffected == 1) {
                        connection.commit();
                        return true;
                    } else {
                        connection.rollback();
                        return false;
                    }
                }
            }
            connection.rollback();
            return false;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (stmtAddress != null) stmtAddress.close();
                if (stmtProperty != null) stmtProperty.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
