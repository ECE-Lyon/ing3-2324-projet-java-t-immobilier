package controllers;

import models.Utilisateur;
import dao.DatabaseConnection;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date; // Importer java.sql.Date pour utiliser Date dans la base de données

public class LoginController {

    private Connection connection;

    public LoginController() {
        // Initialisation de la connexion à la base de données
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

       public Utilisateur getUserByEmail(String email) throws SQLException {
        String query = "SELECT * FROM UTILISATEUR WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id_user"); // Modifier ici pour utiliser le bon nom de colonne
                String userEmail = resultSet.getString("email");
                String password = resultSet.getString("password");
                Date inscriptionDate = resultSet.getDate("inscription_date");
                String userName = resultSet.getString("name");
                boolean status = resultSet.getBoolean("status_user"); // Modifier ici pour utiliser le bon nom de colonne
                return new Utilisateur(id, userEmail, password, inscriptionDate, userName, status);
            } else {
                return null;
            }
        }
    }

    // Méthode pour vérifier l'authentification de l'utilisateur
    public boolean verifyConnection(String email, String password) throws SQLException {
        String query = "SELECT * FROM UTILISATEUR WHERE email = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Renvoie vrai si un utilisateur correspond aux informations fournies
        }
    }

    // Méthode pour créer un utilisateur dans la base de données
    public boolean createUser(String email, String password) throws SQLException {
        String query = "INSERT INTO UTILISATEUR (email, password, inscription_date, name, status_user) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, password);
            statement.setDate(3, new java.sql.Date(System.currentTimeMillis())); // Date du jour
            statement.setString(4, email); // Nom mis comme l'email
            statement.setBoolean(5, false); // Statut initial à 0
            // Exécution de la requête
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
