package dao;

import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientInfoPageDAO {

    // Méthode pour récupérer les informations du client à partir de l'ID et les remplir dans les champs de texte
    public void populateClientInfo(TextField nameField, TextField emailField, TextField passwordField, TextField dateField, TextField statutField, int userId) {
        try {
            // Connexion à la base de données
            Connection connection = DatabaseConnection.getConnection();
            // Requête SQL pour récupérer les informations de l'utilisateur à partir de l'ID
            String query = "SELECT name, email, password, inscription_date, status_user FROM UTILISATEUR WHERE id_user = ?";

            // Préparation de la requête
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);

            // Exécution de la requête
            ResultSet resultSet = preparedStatement.executeQuery();

            // Traitement des résultats de la requête
            if (resultSet.next()) {
                // Remplissage des champs de texte avec les données récupérées
                nameField.setText(resultSet.getString("name"));
                emailField.setText(resultSet.getString("email"));
                passwordField.setText(resultSet.getString("password"));
                dateField.setText(resultSet.getString("inscription_date"));
                statutField.setText(resultSet.getBoolean("status_user") ? "Client" : "Employé");
            }

            // Fermeture des ressources
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour mettre à jour les informations du client dans la base de données
    public void updateClientInfo(TextField nameField, TextField emailField, TextField passwordField, TextField dateField, TextField statutField, int userId) {
        try {
            // Connexion à la base de données
            Connection connection = DatabaseConnection.getConnection();

            // Requête SQL pour mettre à jour les informations du client
            String query = "UPDATE UTILISATEUR SET name = ?, email = ?, password = ?, inscription_date = ?, status_user = ? WHERE id_user = ?";

            // Préparation de la requête
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nameField.getText());
            preparedStatement.setString(2, emailField.getText());
            preparedStatement.setString(3, passwordField.getText());
            preparedStatement.setString(4, dateField.getText());
            preparedStatement.setBoolean(5, statutField.getText().equalsIgnoreCase("Employé")); // Convertir le texte en booléen
            preparedStatement.setInt(6, userId);

            // Exécution de la requête
            preparedStatement.executeUpdate();

            // Fermeture des ressources
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
