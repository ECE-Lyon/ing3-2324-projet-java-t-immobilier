package dao;

import models.Visit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
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

    // Méthode pour récupérer toutes les visites dans la base de données
    public static List<Visit> getAllVisits() {
        List<Visit> visits = new ArrayList<>();
        String sql = "SELECT id_visit, date_visit, time_visit, feedback, id_property, id_user FROM VISIT";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Visit visit = new Visit(
                        rs.getInt("id_visit"),
                        rs.getDate("date_visit"),
                        rs.getTime("time_visit"),
                        rs.getString("feedback"),
                        rs.getInt("id_property"),
                        rs.getInt("id_user")
                );
                visits.add(visit);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des visites : " + e.getMessage());
        }
        return visits;
    }
}