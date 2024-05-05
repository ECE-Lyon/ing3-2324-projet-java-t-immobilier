package views;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import dao.VisitDAO;

import java.time.LocalDate;

public class ProgrammerVisitePage extends Application {
    private int userId;
    private int propertyId;

    private Label confirmationLabelDate;
    private Label confirmationLabelTime;

    public ProgrammerVisitePage(int userId, int propertyId) {
        this.userId = userId;
        this.propertyId = propertyId;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Programmer une visite");

        // Conteneur principal avec une grille pour organiser les éléments
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(20);
        root.setVgap(20);
        root.setPadding(new Insets(20));

        // Définir le fond de la scène en blanc
        root.setStyle("-fx-background-color: #FFFFFF;");

        // Logo de l'agence en haut de la page
        ImageView logoView = new ImageView(new Image("ece_immo.jpeg"));
        logoView.setFitWidth(200); // Ajustez la largeur du logo selon vos besoins
        logoView.setPreserveRatio(true);

        // Centrer le logo horizontalement
        HBox logoContainer = new HBox();
        logoContainer.getChildren().add(logoView);
        logoContainer.setAlignment(Pos.CENTER); // Centrer horizontalement

        // Placez le logo dans la première colonne et la première ligne
        root.add(logoContainer, 0, 0, 2, 1); // Le logo prendra 2 colonnes, centré horizontalement

        // Image sur la partie gauche de l'écran
        ImageView imageView = new ImageView(new Image("maison.png"));
        imageView.setFitWidth(400); // Ajustez la largeur de l'image selon vos besoins
        imageView.setPreserveRatio(true);
        root.add(imageView, 0, 1);

        // Conteneur pour les éléments du formulaire
        VBox formLayout = new VBox();
        formLayout.setAlignment(Pos.CENTER_LEFT);
        formLayout.setSpacing(20);

        // Titre
        Label title = new Label("         Programmer une visite");
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: #333;");

        // Sous-titre
        Label subtitle = new Label("      Choisissez une date et une heure pour votre visite.");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");

        // Formulaire de programmation de visite
        GridPane form = new GridPane();
        form.setAlignment(Pos.CENTER);
        form.setHgap(10);
        form.setVgap(10);

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Date");

        confirmationLabelDate = new Label("Date sélectionnée : ");
        confirmationLabelTime = new Label("Heure sélectionnée : ");
        confirmationLabelDate.setVisible(false);
        confirmationLabelTime.setVisible(false);
        form.add(confirmationLabelDate, 0, 3);
        form.add(confirmationLabelTime, 0, 4);

        ComboBox<String> timeComboBox = new ComboBox<>();
        timeComboBox.setPromptText("Heure");
        timeComboBox.getItems().addAll(
                "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
                "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30",
                "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30"
        );

        Button scheduleButton = new Button("Programmer la visite");
        scheduleButton.setStyle("-fx-background-color: rgb(213, 119, 195); -fx-text-fill: white;");
        scheduleButton.setMaxWidth(Double.MAX_VALUE);

        // Ajout des éléments au formulaire
        form.add(datePicker, 0, 0);
        form.add(timeComboBox, 0, 1);
        form.add(scheduleButton, 0, 2);
        GridPane.setHalignment(scheduleButton, HPos.RIGHT);

        // Ajout des éléments au conteneur du formulaire
        formLayout.getChildren().addAll(title, subtitle, form);

        // Ajout des éléments à la grille principale
        root.add(formLayout, 1, 1);

        // Configuration de la scène
        Scene scene = new Scene(root, 1000, 600); // Taille de la scène ajustée selon vos besoins
        primaryStage.setScene(scene);
        primaryStage.show();

        // Action pour récupérer la date sélectionnée
        datePicker.setOnAction(event -> {
            LocalDate selectedDate = datePicker.getValue();
            System.out.println("Date sélectionnée : " + selectedDate);
        });

        // Action pour récupérer l'heure sélectionnée
        timeComboBox.setOnAction(event -> {
            String selectedTime = timeComboBox.getValue();
            System.out.println("Heure sélectionnée : " + selectedTime);
        });

        scheduleButton.setOnAction(event -> {
            LocalDate selectedDate = datePicker.getValue();
            String selectedTime = timeComboBox.getValue();

            // Vérifiez si une date et une heure ont été sélectionnées
            if (selectedDate != null && selectedTime != null) {
                VisitDAO.insererVisite(selectedDate, selectedTime, propertyId);

                // Afficher les informations de date et d'heure sélectionnées
                confirmationLabelDate.setText("Date sélectionnée : " + selectedDate);
                confirmationLabelTime.setText("Heure sélectionnée : " + selectedTime);
                confirmationLabelDate.setVisible(true);
                confirmationLabelTime.setVisible(true);
            } else {
                System.out.println("Veuillez sélectionner une date et une heure pour programmer la visite.");
            }
        });

    }

    public static void main(String[] args) {
        launch(args);
    }
}