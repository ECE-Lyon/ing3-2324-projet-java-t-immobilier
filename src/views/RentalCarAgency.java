package views;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.*;
import dao.DatabaseConnection;

public class RentalCarAgency extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        root.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        root.setPadding(new Insets(10));
        root.setSpacing(20);
        root.setAlignment(Pos.CENTER);

        // Logo de l'agence de voiture
        ImageView logoImage = new ImageView(new Image(getClass().getResourceAsStream("/Cars.png")));
        logoImage.setFitWidth(200);
        logoImage.setPreserveRatio(true);

        // Titre
        Label titleLabel = new Label("Bienvenue à notre agence de location de voitures");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Informations sur les voitures disponibles
        HBox carInfoBox = createCarInfoBox();

        // Formulaire de réservation
        VBox reservationBox = createReservationBox(primaryStage);

        root.getChildren().addAll(logoImage, titleLabel, carInfoBox, reservationBox);

        // Affichage de la scène
        Scene scene = new Scene(root, 1000, 800);
        primaryStage.setTitle("Agence de location de voitures");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    private HBox createCarInfoBox() {
        HBox carInfoBox = new HBox(20);
        carInfoBox.setAlignment(Pos.CENTER);

        // Image de la voiture Flash McQueen
        ImageView flashMcQueenImage = createCarImage("/voiture1.png");
        Label flashMcQueenLabel = createCarLabel("Flash McQueen", "La star de la course");

        // Image de la voiture Martin
        ImageView martinImage = createCarImage("/voiture2.png");
        Label martinLabel = createCarLabel("Martin", "Le meilleur ami de Flash");

        // Image de la voiture Sally
        ImageView sallyImage = createCarImage("/voiture3.png");
        Label sallyLabel = createCarLabel("Sally", "La juge de la ville");

        carInfoBox.getChildren().addAll(
                createCarPane(flashMcQueenImage, flashMcQueenLabel),
                createCarPane(martinImage, martinLabel),
                createCarPane(sallyImage, sallyLabel)
        );

        return carInfoBox;
    }

    private ImageView createCarImage(String imagePath) {
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
        imageView.setFitWidth(200);
        imageView.setFitHeight(150); // Redimensionne les images de la même taille
        imageView.setPreserveRatio(true);
        return imageView;
    }

    private Label createCarLabel(String carName, String description) {
        Label label = new Label(carName + "\n" + description);
        label.setStyle("-fx-font-size: 14px;");
        label.setAlignment(Pos.CENTER);
        return label;
    }

    private VBox createCarPane(ImageView imageView, Label label) {
        VBox carPane = new VBox(10);
        carPane.setAlignment(Pos.CENTER);
        carPane.getChildren().addAll(imageView, label);
        return carPane;
    }

    private VBox createReservationBox(Stage primaryStage) {
        VBox reservationBox = new VBox(20);
        reservationBox.setAlignment(Pos.CENTER);

        Label reservationLabel = new Label("Réserver une voiture");
        reservationLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField nameTextField = createTextField("Nom");
        TextField emailTextField = createTextField("Email");
        TextField carNumberTextField = createTextField("Numéro de voiture");

        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Date de début de location");

        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setPromptText("Date de fin de location");

        Button reserveButton = new Button("Réserver");
        reserveButton.setOnAction(event -> {
            String name = nameTextField.getText();
            String email = emailTextField.getText();
            String carNumber = carNumberTextField.getText();
            String startDate = startDatePicker.getValue().toString();
            String endDate = endDatePicker.getValue().toString();
            System.out.println("Réservation : Nom = " + name + ", Email = " + email + ", Numéro de voiture = " + carNumber
                    + ", Date de début = " + startDate + ", Date de fin = " + endDate);
            showReservationConfirmation(primaryStage);
        });

        // Récupération des informations de l'utilisateur à partir de la base de données
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT name, email FROM UTILISATEUR WHERE id_user = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            // Remplacez "1" par l'ID de l'utilisateur connecté
            statement.setInt(1, 1);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                nameTextField.setText(name);
                emailTextField.setText(email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestion des erreurs de connexion à la base de données
        }

        reservationBox.getChildren().addAll(reservationLabel, nameTextField, emailTextField, carNumberTextField, startDatePicker, endDatePicker, reserveButton);

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
        alert.setHeaderText("L'agence Cars vous remercie !");
        alert.setContentText("Votre réservation a été effectuée avec succès. Bonne route !");
        alert.showAndWait();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
