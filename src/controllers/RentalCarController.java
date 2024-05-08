package controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import views.RentalCarAgency;
import dao.ReservationDAO;

public class RentalCarController {

    private Stage primaryStage;
    private int userId;

    public RentalCarController(Stage primaryStage, int userId) {
        this.primaryStage = primaryStage;
        this.userId = userId;
    }

    public void reserveCar(String name, String email, String carNumber, String startDate, String endDate) {
        ReservationDAO.insertReservationCar(name, email, carNumber, startDate, endDate);
        showReservationConfirmation();
    }

    private void showReservationConfirmation() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation de réservation");
        alert.setHeaderText("L'agence Cars vous remercie !");
        alert.setContentText("Votre réservation a été effectuée avec succès. Bonne route !");
        alert.showAndWait();
    }
}
