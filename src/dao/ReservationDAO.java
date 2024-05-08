package dao;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReservationDAO {

    public static void insertReservation(String name, String email, String cinemaNumber, String startTime) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO RESERVATION (nom, email, nom_film, date_debut) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, cinemaNumber);
            statement.setString(4, startTime);
            statement.executeUpdate();
        }
    }
    public static void retrieveUserInfo(int userId, TextField nameTextField, TextField emailTextField) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String userQuery = "SELECT name, email FROM UTILISATEUR WHERE id_user = ?";
            PreparedStatement userStatement = connection.prepareStatement(userQuery);
            userStatement.setInt(1, userId);
            ResultSet userResultSet = userStatement.executeQuery();
            if (userResultSet.next()) {
                String name = userResultSet.getString("name");
                String email = userResultSet.getString("email");
                nameTextField.setText(name);
                emailTextField.setText(email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestion des erreurs de connexion à la base de données
        }
    }

    public static void retrieveFilmNumbers(ComboBox<String> cinemaNumberComboBox) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String cinemaNumberQuery = "SELECT DISTINCT nom_film FROM RESERVATION";
            PreparedStatement cinemaNumberStatement = connection.prepareStatement(cinemaNumberQuery);
            ResultSet carNumberResultSet = cinemaNumberStatement.executeQuery();
            while (carNumberResultSet.next()) {
                String carNumber = carNumberResultSet.getString("nom_film");
                cinemaNumberComboBox.getItems().add(carNumber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestion des erreurs de connexion à la base de données
        }
    }

    public static void retrieveCarNumbers(ComboBox<String> carNumberComboBox) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String carNumberQuery = "SELECT DISTINCT numero_voiture FROM RESERVATION";
            PreparedStatement carNumberStatement = connection.prepareStatement(carNumberQuery);
            ResultSet carNumberResultSet = carNumberStatement.executeQuery();
            while (carNumberResultSet.next()) {
                String carNumber = carNumberResultSet.getString("numero_voiture");
                carNumberComboBox.getItems().add(carNumber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestion des erreurs de connexion à la base de données
        }
    }
    public static void insertReservationCar(String name, String email, String carNumber, String startDate, String endDate) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO RESERVATION (nom, email, numero_voiture, date_debut, date_fin) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, carNumber);
            statement.setDate(4, java.sql.Date.valueOf(startDate));
            statement.setDate(5, java.sql.Date.valueOf(endDate));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestion des erreurs de connexion à la base de données ou d'insertion
        }
    }
}
