package views;

import controllers.RentalCinemaController;
import dao.ReservationDAO;
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

public class RentalCinemaAgency extends Application {

    private int userId;

    public RentalCinemaAgency(int userId) {
        this.userId = userId;
    }

    @Override
    public void start(Stage primaryStage) {
        RentalCinemaController controller = new RentalCinemaController(primaryStage, userId);

        VBox root = new VBox();
        root.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        root.setPadding(new Insets(10));
        root.setSpacing(20);
        root.setAlignment(Pos.CENTER);

        ImageView logoImage = new ImageView(new Image(getClass().getResourceAsStream("/Salle_cinema.jpg")));
        logoImage.setFitWidth(200);
        logoImage.setPreserveRatio(true);

        Label titleLabel = new Label("Bienvenue à notre agence de location de films");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        HBox cinemaInfoBox = createCinemaInfoBox();

        VBox reservationBox = createReservationBox(primaryStage, controller);

        root.getChildren().addAll(logoImage, titleLabel, cinemaInfoBox, reservationBox);

        primaryStage.setScene(new Scene(root, 1000, 800));
        primaryStage.setTitle("Agence de location de salles de films");
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    private HBox createCinemaInfoBox() {
        HBox cinemaInfoBox = new HBox(20);
        cinemaInfoBox.setAlignment(Pos.CENTER);

        ImageView cinema1Image = createCinemaImage("/cinema_image1.jpg");
        Label cinema1Label = createCinemaLabel("Salle 1", "Salle équipée en 3D\nIdéale pour les films d'action");

        ImageView cinema2Image = createCinemaImage("/cinema_image2.jpg");
        Label cinema2Label = createCinemaLabel("Salle 2", "Salle pour les événements privés\nConvient aux projections exclusives");

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
        imageView.setFitHeight(150);
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

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(imageView);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        cinemaPane.getChildren().addAll(borderPane, spacer, label);

        return cinemaPane;
    }


    private VBox createReservationBox(Stage primaryStage, RentalCinemaController controller) {
        VBox reservationBox = new VBox(20);
        reservationBox.setAlignment(Pos.CENTER);

        Label reservationLabel = new Label("Réserver un film au cinéma");
        reservationLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField nameTextField = createTextField("Nom");
        TextField emailTextField = createTextField("Email");

        ComboBox<String> cinemaNumberComboBox = new ComboBox<>();
        cinemaNumberComboBox.setPromptText("Sélectionnez un film");

        // Appel à ReservationDAO pour récupérer les informations utilisateur et les numéros de film
        ReservationDAO.retrieveUserInfo(userId, nameTextField, emailTextField);
        ReservationDAO.retrieveFilmNumbers(cinemaNumberComboBox);

        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Heure de séance");

        Button reserveButton = new Button("Réserver");
        reserveButton.setOnAction(event -> {
            String name = nameTextField.getText();
            String email = emailTextField.getText();
            String cinemaNumber = cinemaNumberComboBox.getValue();
            LocalDate startDate = startDatePicker.getValue();

            if (startDate != null) {
                String startTime = startDate.toString();
                controller.reserveFilm(name, email, cinemaNumber, startTime);
            } else {
                System.out.println("Veuillez sélectionner une date de séance.");
            }
        });

        reservationBox.getChildren().addAll(reservationLabel, nameTextField, emailTextField, cinemaNumberComboBox, startDatePicker, reserveButton);

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
