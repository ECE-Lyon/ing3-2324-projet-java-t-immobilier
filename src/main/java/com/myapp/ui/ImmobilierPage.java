package com.myapp.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ImmobilierPage extends Application {
    private static final double CARD_WIDTH = 400.0;

    @Override
    public void start(Stage primaryStage) {
        // Créer le header avec des options de filtrage
        HBox headerBox = createHeader();

        // GridPane pour les annonces
        GridPane annoncesGrid = new GridPane();
        annoncesGrid.setHgap(20); // Espacement horizontal entre les cartes
        annoncesGrid.setVgap(20); // Espacement vertical entre les cartes
        annoncesGrid.setAlignment(Pos.CENTER);
        annoncesGrid.setPadding(new Insets(25, 25, 25, 25));

        // Création des annonces
        int numCols = 2; // Nombre de colonnes
        int numRows = 8; // Nombre de lignes (selon le besoin)
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                VBox annonce = createAnnonce("Villa Porto-Vecchio", "5 pièces - 4 chambres - 145m²", "image2.jpeg");
                annoncesGrid.add(annonce, j, i); // Ajoutez les annonces à la grille
            }
        }

        // ScrollPane pour les annonces
        ScrollPane scrollPane = new ScrollPane(annoncesGrid);
        scrollPane.setFitToWidth(true);

        // BorderPane pour organiser le header et les annonces
        BorderPane root = new BorderPane();
        root.setTop(headerBox);
        root.setCenter(scrollPane);

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

        Image image = new Image(imagePath);
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

    private HBox createHeader() {
        HBox headerBox = new HBox(20);
        headerBox.setPadding(new Insets(15));
        headerBox.setStyle("-fx-background-color: #336699;");
        headerBox.setAlignment(Pos.CENTER);

        Button homeButton = new Button("Accueil");
        styleButton(homeButton);
        Button propertyButton = new Button("Propriétés");
        styleButton(propertyButton);
        Button contactButton = new Button("Contact");
        styleButton(contactButton);

        headerBox.getChildren().addAll(homeButton, propertyButton, contactButton);

        return headerBox;
    }

    private void styleButton(Button button) {
        button.setStyle("-fx-background-color: white; -fx-text-fill: #336699;");
        button.setFont(Font.font("Arial", 14));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
