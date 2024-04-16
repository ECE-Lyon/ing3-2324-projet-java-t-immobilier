package views;

import models.Utilisateur;

import dao.DatabaseConnection;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.*;
import java.util.Date;
import java.util.List;


public class ModernUIApp extends Application {
    public ObservableList<String> getCitiesFromDatabase() {
        ObservableList<String> cities = FXCollections.observableArrayList();

        String query = "SELECT city FROM ADDRESS"; // Requête SQL pour récupérer les villes

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet resultSet = stmt.executeQuery()) {

            while (resultSet.next()) {
                cities.add(resultSet.getString("city")); // Ajoute chaque ville à la liste
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cities;
    }

    @Override
    public void start(Stage primaryStage) {

        Utilisateur user = Utilisateur.getCurrentUser();

        // Info utilisateur Recup après connexion
        int userId = user.getId();
        String userEmail = user.getEmail();
        String userPassword = user.getPassword();
        Date userInscriptionDate = user.getInscriptionDate();
        String userName = user.getName();
        boolean userStatus = user.getStatus();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: white;");

        // Logo de l'agence
        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/ece_immo.jpeg")));
        logo.setFitWidth(120);
        logo.setPreserveRatio(true);

        // MenuBar avec fond blanc et éléments agrandis
        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color: white; -fx-font-size: 18px; -fx-padding: 15px 0; -fx-border-color: lightgrey; -fx-border-width: 0 0 1 0;");

        // Menus avec items déroulants
        Menu menuImmobilier = new Menu("Immobilier");
        menuImmobilier.getItems().addAll(new MenuItem("Acheter"), new MenuItem("Vendre"), new MenuItem("Louer"));
        Menu menuArtDeVivre = new Menu("Art de Vivre");
        menuArtDeVivre.getItems().addAll(new MenuItem("Culture"), new MenuItem("Gastronomie"), new MenuItem("Voyages"));
        Menu menuServices = new Menu("Services");
        menuServices.getItems().addAll(new MenuItem("Conseils"), new MenuItem("Estimations"), new MenuItem("Financements"));
        menuBar.getMenus().addAll(menuImmobilier, menuArtDeVivre, menuServices);

        // Icône de profil à droite
        ImageView profileIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon.jpg")));
        profileIcon.setFitWidth(30);
        profileIcon.setFitHeight(30);
        Button profileButton = new Button("", profileIcon);
        profileButton.setStyle("-fx-background-color: transparent;");
        HBox.setMargin(profileButton, new Insets(0, 0, 0, 20));

        // Gestionnaire d'événements pour le clic sur l'icône de profil
        /*profileButton.setOnAction(event -> {
            Stage profileStage = new Stage();
            ClientInfoPage clientInfoPage = new ClientInfoPage(userId); // Passer l'ID de l'utilisateur
            clientInfoPage.start(profileStage);
        });*/
        profileButton.setOnAction(event -> {
            Stage profileStage = new Stage();
            MonProfilPage monProfilPage = new MonProfilPage();
            monProfilPage.start(profileStage);
        });



        // Layout pour la MenuBar et l'icône de profil
        HBox menuContainer = new HBox(menuBar, profileButton);
        menuContainer.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(menuBar, Priority.ALWAYS);
        menuContainer.setPadding(new Insets(0, 0, 0, 20));

        // Layout pour le logo, la MenuBar et l'icône de profil
        HBox topBar = new HBox(logo, menuContainer);
        topBar.setAlignment(Pos.CENTER);
        topBar.setSpacing(20);

        root.setTop(topBar);

        // Carrousel d'images
        List<String> imagePaths = List.of("/image2.jpeg", "/image3.jpeg", "/image4.jpeg");
        CarouselWithTimeline customCarousel = new CarouselWithTimeline(imagePaths, Duration.seconds(5));

        StackPane imageContainer = new StackPane(customCarousel);
        imageContainer.setStyle("-fx-background-color: white;");
        root.setCenter(imageContainer);

        // Barre de recherche au bas du carrousel
        HBox searchBox = createSearchBox();
        imageContainer.getChildren().add(searchBox);
        StackPane.setAlignment(searchBox, Pos.BOTTOM_CENTER);

        // Ajuster la disposition du BorderPane pour que le carrousel occupe toute la largeur de l'écran
        VBox vbox = new VBox();
        vbox.setSpacing(20);
        vbox.getChildren().addAll(topBar, imageContainer);
        VBox.setVgrow(imageContainer, Priority.ALWAYS);
        root.setCenter(vbox);

        // Définir la largeur du carrousel et mettre à jour la largeur de l'image view
        Scene scene = new Scene(root, 1920, 1080);
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            customCarousel.setPrefWidth(newValue.doubleValue());
            customCarousel.updateImageViewWidth();
        });
        customCarousel.setPrefSize(scene.getWidth(), 768);
        customCarousel.updateImageViewWidth();


        // Pied de page
        HBox footerLayout = createFooter();
        root.setBottom(footerLayout);

        // Configuration et affichage de la scène
        scene.setFill(Color.WHITE);
        primaryStage.setTitle("ECE International Realty - Accueil");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Afficher le nom de l'utilisateur dans la barre de menu ou ailleurs dans l'interface
        Label userLabel = new Label("Connecté en tant que : " + userName);
        userLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");
        menuContainer.getChildren().add(userLabel);
    }

    private HBox createSearchBox() {
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER);
        searchBox.setPadding(new Insets(15));
        searchBox.setStyle("-fx-background-color: rgba(176,165,165,0.43); -fx-background-radius: 5;");

        ComboBox<String> comboBoxCity = new ComboBox<>();
        comboBoxCity.setItems(getCitiesFromDatabase());
        comboBoxCity.setPromptText("Ville");

        ComboBox<String> comboBoxBuyRent = new ComboBox<>();
        comboBoxBuyRent.getItems().addAll("Acheter", "Louer");
        comboBoxBuyRent.setPromptText("Type d'achat");

        TextField textFieldPropertyType = new TextField();
        textFieldPropertyType.setPromptText("Type de bien");

        TextField textFieldBudget = new TextField();
        textFieldBudget.setPromptText("Budget");

        Button buttonSearch = new Button("Rechercher");
        buttonSearch.setStyle("-fx-background-color: darkblue; -fx-text-fill: white;");

        searchBox.getChildren().addAll(comboBoxCity, comboBoxBuyRent, textFieldPropertyType, textFieldBudget, buttonSearch);

        return searchBox;
    }

    private HBox createFooter() {
        HBox footerLayout = new HBox();
        footerLayout.setAlignment(Pos.CENTER);
        footerLayout.setStyle("-fx-background-color: #333;");
        Label footerText = new Label("© 2024 ECE International Realty");
        footerText.setStyle("-fx-text-fill: white;");
        footerLayout.getChildren().add(footerText);

        return footerLayout;
    }

    public static void main(String[] args) {
        launch(args);
    }
    public static void launchApp(Stage stage) {
        ModernUIApp app = new ModernUIApp();
        app.start(stage);
    }

}

class CarouselWithTimeline extends StackPane {
    private final List<String> imagePaths;
    private final Duration delay;
    private final IntegerProperty currentIndex = new SimpleIntegerProperty(0);
    private final ProgressBar progressBar;
    private final Timeline carouselTimeline;

    public CarouselWithTimeline(List<String> imagePaths, Duration delay) {
        this.imagePaths = imagePaths;
        this.delay = delay;

        getChildren().addAll(createImageView(), createDreamPropertyLabel()); // Ajoutez le label au carrousel

        progressBar = new ProgressBar();
        progressBar.setPrefWidth(200);

        carouselTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, event -> {
                    progressBar.setProgress(0);
                }),
                new KeyFrame(delay)
        );
        carouselTimeline.setCycleCount(Animation.INDEFINITE);
        carouselTimeline.play();

        Timeline progressBarTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, event -> {
                    progressBar.setProgress(0);
                }),
                new KeyFrame(delay, event -> {
                    int newIndex = (currentIndex.get() + 1) % imagePaths.size();
                    currentIndex.set(newIndex);
                    getChildren().set(0, createImageView());
                })
        );
        progressBarTimeline.setCycleCount(Animation.INDEFINITE);
        progressBarTimeline.play();
    }

    private ImageView createImageView() {
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(imagePaths.get(currentIndex.get()))));
        imageView.setFitWidth(getPrefWidth());
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        return imageView;
    }

    private Label createDreamPropertyLabel() {
        Label dreamPropertyLabel = new Label("Trouvez votre propriété de rêve...");
        dreamPropertyLabel.setStyle("-fx-font-size: 26px; -fx-text-fill: white; -fx-font-family: 'Arial';");
        dreamPropertyLabel.setAlignment(Pos.CENTER);
        dreamPropertyLabel.setPrefWidth(getPrefWidth());
        dreamPropertyLabel.setTranslateY(-350); // Déplacez le texte vers le haut
        dreamPropertyLabel.setEffect(new DropShadow(2, Color.BLACK)); // Ajoutez une ombre
        return dreamPropertyLabel;
    }

    public void updateImageViewWidth() {
        if (getChildren().size() > 0 && getChildren().get(0) instanceof ImageView) {
            ImageView imageView = (ImageView) getChildren().get(0);
            imageView.setFitWidth(getPrefWidth());
        }
    }

    public void pauseCarousel() {
        carouselTimeline.pause();
    }

    public void resumeCarousel() {
        carouselTimeline.play();
    }
}
