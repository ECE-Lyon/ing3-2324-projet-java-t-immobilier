package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class VisitDAO {

    // Méthode pour insérer une nouvelle visite dans la base de données
    public static void insererVisite(LocalDate date, String heure, int idPropriete, int idClient) {
        try (Connection connexion = DatabaseConnection.getConnection()) {
            // Préparez la requête SQL pour insérer une nouvelle visite
            String requete = "INSERT INTO VISIT (date_visit, time_visit, id_property, id_user) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connexion.prepareStatement(requete)) {
                // Définissez les valeurs des paramètres dans la requête SQL
                preparedStatement.setDate(1, java.sql.Date.valueOf(date));
                preparedStatement.setString(2, heure);
                preparedStatement.setInt(3, idPropriete);
                preparedStatement.setInt(4, idClient);


                // Exécutez la requête SQL
                preparedStatement.executeUpdate();
                System.out.println("La visite a été programmée avec succès !");
            } catch (SQLException e) {
                System.out.println("Erreur lors de l'insertion de la visite : " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Erreur de connexion à la base de données : " + e.getMessage());
        }
    }
}
