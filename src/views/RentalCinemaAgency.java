package views;

import dao.DatabaseConnection;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.LocalDate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RentalCinemaAgency extends Application {

    private int userId; // Ajout de l'ID de l'utilisateur

    public RentalCinemaAgency(int userId) {
        this.userId = userId;
    }

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        root.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        root.setPadding(new Insets(10));
        root.setSpacing(20);
        root.setAlignment(Pos.CENTER);

        // Logo de l'agence de cinéma
        ImageView logoImage = new ImageView(new Image(getClass().getResourceAsStream("/Salle_cinema.jpg")));
        logoImage.setFitWidth(200);
        logoImage.setPreserveRatio(true);

        // Titre
        Label titleLabel = new Label("Bienvenue à notre agence de location de films");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Informations sur les salles de cinéma disponibles
        HBox cinemaInfoBox = createCinemaInfoBox();

        // Formulaire de réservation
        VBox reservationBox = createReservationBox(primaryStage);

        root.getChildren().addAll(logoImage, titleLabel, cinemaInfoBox, reservationBox);

        // Affichage de la scène
        Scene scene = new Scene(root, 1000, 800);
        primaryStage.setTitle("Agence de location de salles de films");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    private HBox createCinemaInfoBox() {
        HBox cinemaInfoBox = new HBox(20);
        cinemaInfoBox.setAlignment(Pos.CENTER);

        // Image de la salle de cinéma 1
        ImageView cinema1Image = createCinemaImage("/cinema_image1.jpg");
        Label cinema1Label = createCinemaLabel("Salle 1", "Salle équipée en 3D\nIdéale pour les films d'action");

        // Image de la salle de cinéma 2
        ImageView cinema2Image = createCinemaImage("/cinema_image2.jpg");
        Label cinema2Label = createCinemaLabel("Salle 2", "Salle pour les événements privés\nConvient aux projections exclusives");

        // Image de la salle de cinéma 3
        ImageView cinema3Image = createCinemaImage("/cinema_image3.jpg");
        Label cinema3Label = createCinemaLabel("Salle 3", "Salle avec sièges inclinables\nConfort ultime pour une expérience cinématographique");

        cinemaInfoBox.getChildren().addAll(
                createCinemaPane(cinema1Image, cinema1Label),
                createCinemaPane(cinema2Image, cinema2Label),
                createCinemaPane(cinema3Image, cinema3Label)
        );

        return cinemaInfoBox;
    }

    private ImageView createCinemaImage(String imagePath) {
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
        imageView.setFitWidth(200);
        imageView.setFitHeight(150); // Redimensionne les images de la même taille
        imageView.setPreserveRatio(true);
        return imageView;
    }

    private Label createCinemaLabel(String cinemaName, String description) {
        Label label = new Label(cinemaName + "\n" + description);
        label.setStyle("-fx-font-size: 14px;");
        label.setAlignment(Pos.CENTER);
        return label;
    }

    private VBox createCinemaPane(ImageView imageView, Label label) {
        VBox cinemaPane = new VBox(10);
        cinemaPane.setAlignment(Pos.CENTER);

        // Création d'un BorderPane pour encadrer l'image et sa description
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(imageView);

        // Création d'une région vide pour séparer l'image et le texte
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // Ajout de la région vide pour créer un espacement vertical
        cinemaPane.getChildren().addAll(borderPane, spacer, label);

        return cinemaPane;
    }


    private VBox createReservationBox(Stage primaryStage) {
        VBox reservationBox = new VBox(20);
        reservationBox.setAlignment(Pos.CENTER);

        Label reservationLabel = new Label("Réserver un film au cinéma");
        reservationLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField nameTextField = createTextField("Nom");
        TextField emailTextField = createTextField("Email");

        ComboBox<String> cinemaNumberComboBox = new ComboBox<>();
        cinemaNumberComboBox.setPromptText("Sélectionnez un film");

        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Heure de séance");

        Button reserveButton = new Button("Réserver");
        reserveButton.setOnAction(event -> {
            String name = nameTextField.getText();
            String email = emailTextField.getText();
            String cinemaNumber = cinemaNumberComboBox.getValue();
            LocalDate startDate = startDatePicker.getValue();

            // Vérifier si startDate est null
            if (startDate != null) {
                String startTime = startDate.toString(); // Heure de début uniquement
                // Exécuter la requête d'insertion
                try (Connection connection = DatabaseConnection.getConnection()) {
                    String query = "INSERT INTO RESERVATION (nom, email, nom_film, date_debut) VALUES (?, ?, ?, ?)";
                    //String query = "INSERT INTO RESERVATION (nom, email, nom_film, date_debut, id_client) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, name);
                    statement.setString(2, email);
                    statement.setString(3, cinemaNumber);
                    statement.setDate(4, java.sql.Date.valueOf(startTime));
                    //statement.setInt(5, id_user);
                    statement.executeUpdate();
                    System.out.println("Réservation enregistrée dans la base de données avec succès.");
                    showReservationConfirmation(primaryStage);
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Gestion des erreurs de connexion à la base de données ou d'insertion
                }
            } else {
                // Gérer le cas où aucune date n'est sélectionnée
                System.out.println("Veuillez sélectionner une date de séance.");
                // Afficher un message d'erreur ou effectuer une action appropriée
            }
        });



        // Récupération des informations de l'utilisateur à partir de la base de données
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Requête pour récupérer le nom et l'email de l'utilisateur
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

            // Requête pour récupérer les numéros de voiture disponibles
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

        reservationBox.getChildren().addAll(reservationLabel, nameTextField, emailTextField, cinemaNumberComboBox, startDatePicker, reserveButton);

        return reservationBox;
    }

    private TextField createTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setPrefWidth(300);
        return textField;
    }

    private void showReservationConfirmation(Stage primaryStage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation de réservation");
        alert.setHeaderText("L'agence de cinéma vous remercie !");
        alert.setContentText("Votre réservation a été effectuée avec succès. Bonne séance !");
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
