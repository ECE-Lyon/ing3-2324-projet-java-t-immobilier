package views;

import controllers.RentalCarController;
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
import dao.ReservationDAO;

public class RentalCarAgency extends Application {

    private int userId;

    public RentalCarAgency(int userId) {
        this.userId = userId;
    }

    @Override
    public void start(Stage primaryStage) {
        RentalCarController controller = new RentalCarController(primaryStage, userId);

        VBox root = new VBox();
        root.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        root.setPadding(new Insets(10));
        root.setSpacing(20);
        root.setAlignment(Pos.CENTER);

        ImageView logoImage = new ImageView(new Image(getClass().getResourceAsStream("/Cars.png")));
        logoImage.setFitWidth(200);
        logoImage.setPreserveRatio(true);

        Label titleLabel = new Label("Bienvenue à notre agence de location de voitures");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        HBox carInfoBox = createCarInfoBox();

        VBox reservationBox = createReservationBox(primaryStage, controller);

        root.getChildren().addAll(logoImage, titleLabel, carInfoBox, reservationBox);

        Scene scene = new Scene(root, 1000, 800);
        primaryStage.setTitle("Agence de location de voitures");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    private HBox createCarInfoBox() {
        HBox carInfoBox = new HBox(20);
        carInfoBox.setAlignment(Pos.CENTER);

        ImageView flashMcQueenImage = createCarImage("/voiture1.png");
        Label flashMcQueenLabel = createCarLabel("Flash McQueen", "La star de la course");

        ImageView martinImage = createCarImage("/voiture2.png");
        Label martinLabel = createCarLabel("Martin", "Le meilleur ami de Flash");

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
        imageView.setFitHeight(150);
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

    private VBox createReservationBox(Stage primaryStage, RentalCarController controller) {
        VBox reservationBox = new VBox(20);
        reservationBox.setAlignment(Pos.CENTER);

        Label reservationLabel = new Label("Réserver une voiture");
        reservationLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField nameTextField = createTextField("Nom");
        TextField emailTextField = createTextField("Email");
        ComboBox<String> carNumberComboBox = new ComboBox<>();
        carNumberComboBox.setPromptText("Sélectionnez un numéro de voiture");
        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Date de début de location");

        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setPromptText("Date de fin de location");

        Button reserveButton = new Button("Réserver");
        reserveButton.setOnAction(event -> {
            String name = nameTextField.getText();
            String email = emailTextField.getText();
            String carNumber = carNumberComboBox.getValue();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();

            if (startDate != null && endDate != null) {
                String startDateString = startDate.toString();
                String endDateString = endDate.toString();
                controller.reserveCar(name, email, carNumber, startDateString, endDateString);
            } else {
                System.out.println("Veuillez sélectionner une date de début et de fin de location.");
            }
        });

        ReservationDAO.retrieveUserInfo(userId, nameTextField, emailTextField);
        ReservationDAO.retrieveCarNumbers(carNumberComboBox);

        reservationBox.getChildren().addAll(reservationLabel, nameTextField, emailTextField, carNumberComboBox, startDatePicker, endDatePicker, reserveButton);

        return reservationBox;
    }

    private TextField createTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setPrefWidth(300);
        return textField;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
