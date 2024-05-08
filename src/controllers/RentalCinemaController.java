package controllers;

import dao.ReservationDAO;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.sql.SQLException;

public class RentalCinemaController {

    private Stage primaryStage;
    private int userId;

    public RentalCinemaController(Stage primaryStage, int userId) {
        this.primaryStage = primaryStage;
        this.userId = userId;
    }

    public void reserveFilm(String name, String email, String cinemaNumber, String startTime) {
        try {
            ReservationDAO.insertReservation(name, email, cinemaNumber, startTime);
            showReservationConfirmation();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs d'insertion
        }
    }

    private void showReservationConfirmation() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation de réservation");
        alert.setHeaderText("L'agence de cinéma vous remercie !");
        alert.setContentText("Votre réservation a été effectuée avec succès. Bonne séance !");
        alert.showAndWait();
    }
}
