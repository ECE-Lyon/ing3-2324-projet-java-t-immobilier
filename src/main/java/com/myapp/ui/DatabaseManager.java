package com.myapp.ui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {

    // Méthode pour mettre à jour les informations de l'utilisateur dans la base de données
    public static void updateUser(Utilisateur user) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // Établir une connexion à la base de données
            connection = DatabaseConnection.getConnection();

            // Préparer la requête SQL
            String query = "UPDATE UTILISATEUR SET email = ?, name = ? WHERE id_user = ?";
            statement = connection.prepareStatement(query);

            // Définir les valeurs des paramètres dans la requête SQL
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getName());
            statement.setInt(3, user.getId());

            // Exécuter la requête de mise à jour
            statement.executeUpdate();
        } finally {
            // Fermer la connexion et la déclaration
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
}
