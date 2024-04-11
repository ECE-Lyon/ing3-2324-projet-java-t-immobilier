
package com.myapp.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ImmobilierPage extends Application {
    private static final double CARD_WIDTH = 400.0;

    @Override
    public void start(Stage primaryStage) {
        // Logo de l'agence
        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/ece_immo.jpeg")));
        logo.setFitWidth(120);
        logo.setPreserveRatio(true);

        // MenuBar avec fond blanc et éléments agrandis
        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color: white; -fx-font-size: 18px; -fx-padding: 15px 0; -fx-border-color: #f6f6f6; -fx-border-width: 0 0 1 0;");

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

        // HBox pour organiser le logo, les menus et l'icône de profil
        HBox logoMenuBarBox = new HBox(10);
        logoMenuBarBox.setStyle("-fx-background-color: WHITE;");
        logoMenuBarBox.setPadding(new Insets(15));
        logoMenuBarBox.setAlignment(Pos.CENTER);
        logoMenuBarBox.getChildren().addAll(logo, menuBar, profileButton);
        HBox.setHgrow(menuBar, Priority.ALWAYS); // Permet à la barre de menu de prendre autant d'espace horizontal que possible

        // New Filters to match the image
        ChoiceBox<String> transactionTypeChoiceBox = new ChoiceBox<>();
        transactionTypeChoiceBox.getItems().addAll("Acheter", "Louer", "Viager");
        transactionTypeChoiceBox.setValue("Acheter");

        ChoiceBox<String> propertyTypeChoiceBox = new ChoiceBox<>();
        propertyTypeChoiceBox.getItems().addAll("Appartement", "Maison", "Terrain", "Commerce", "Bureau");
        propertyTypeChoiceBox.setValue("Types de bien");

        TextField budgetTextField = new TextField();
        budgetTextField.setPromptText("Budget");

        TextField bedroomsTextField = new TextField();
        bedroomsTextField.setPromptText("Chambres");

        TextField areaTextField = new TextField();
        areaTextField.setPromptText("Surface");

        Button advancedSearchButton = new Button("Recherche avancée");
        advancedSearchButton.setStyle("-fx-background-color: rgb(213, 119, 195); -fx-text-fill: white;");

        // HBox for filters
        HBox filtersBox = new HBox(10, transactionTypeChoiceBox, propertyTypeChoiceBox, budgetTextField, bedroomsTextField, areaTextField, advancedSearchButton);
        filtersBox.setAlignment(Pos.CENTER);
        filtersBox.setSpacing(5);

        // VBox pour organiser le contenu du haut (logo, menus, filtres)
        VBox topContent = new VBox(logoMenuBarBox, filtersBox);
        topContent.setAlignment(Pos.CENTER);
        topContent.setSpacing(10);
        topContent.setPadding(new Insets(10));

        // GridPane pour les annonces
        GridPane annoncesGrid = new GridPane();
        annoncesGrid.setHgap(20); // Espacement horizontal entre les cartes
        annoncesGrid.setVgap(20); // Espacement vertical entre les cartes
        annoncesGrid.setAlignment(Pos.CENTER);
        annoncesGrid.setPadding(new Insets(25, 25, 25, 25));

        // Création des annonces
        int numCols = 3; // Nombre de colonnes
        int numRows = 4; // Nombre de lignes (selon le besoin)
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                VBox annonce = createAnnonce("Villa Porto-Vecchio", "5 pièces - 4 chambres - 145m²", "/image2.jpeg");
                annoncesGrid.add(annonce, j, i); // Ajoutez les annonces à la grille
            }
        }

        // ScrollPane pour les annonces
        ScrollPane scrollPane = new ScrollPane(annoncesGrid);
        scrollPane.setFitToWidth(true);

        // BorderPane pour organiser le contenu
        BorderPane root = new BorderPane();
        root.setTop(topContent);
        root.setCenter(scrollPane);

        // Search button
        Button searchButton = new Button("Rechercher");
        searchButton.setStyle("-fx-background-color: rgb(213, 119, 195); -fx-text-fill: white;");
        root.setBottom(searchButton);
        BorderPane.setAlignment(searchButton, Pos.CENTER);
        BorderPane.setMargin(searchButton, new Insets(10));

        // Affichage de la scène
        Scene scene = new Scene(root, 1024, 768); // Taille par défaut, peut être ajustée ou mise en plein écran
        primaryStage.setScene(scene);
        primaryStage.setTitle("Liste des propriétés immobilières");
        primaryStage.show();
    }

    private VBox createAnnonce(String title, String details, String imagePath) {
        VBox annonceBox = new VBox(10);
        annonceBox.setPadding(new Insets(15));
        annonceBox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #c0c0c0;");
        annonceBox.setAlignment(Pos.CENTER_LEFT);

        Image image = new Image(getClass().getResourceAsStream(imagePath));
        double imageWidth = CARD_WIDTH - 30; // Soustrayez la valeur de padding pour maintenir la largeur de la carte
        double imageHeight = imageWidth * (image.getHeight() / image.getWidth());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(imageWidth);
        imageView.setFitHeight(imageHeight);

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", 20));

        Label detailsLabel = new Label(details);
        detailsLabel.setFont(Font.font("Arial", 16));

        Button visitButton = new Button("Programmer une visite");
        visitButton.setStyle("-fx-background-color: rgb(213, 119, 195); -fx-text-fill: white;");

        annonceBox.getChildren().addAll(imageView, titleLabel, detailsLabel, visitButton);

        return annonceBox;
    }

    public static void main(String[] args) {
        launch(args);
    }
}