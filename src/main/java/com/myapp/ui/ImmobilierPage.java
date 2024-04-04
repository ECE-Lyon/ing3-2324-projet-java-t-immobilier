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
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class ImmobilierPage extends Application {
    @Override
    public void start(Stage primaryStage) {
        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-background-color: #f4f4f4;");

        // Titre de la page
        Label titleLabel = new Label("Bienvenue chez ECE International Realty");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextAlignment(TextAlignment.CENTER);
        AnchorPane.setTopAnchor(titleLabel, 20.0);
        AnchorPane.setLeftAnchor(titleLabel, 0.0);
        AnchorPane.setRightAnchor(titleLabel, 0.0);

        // Annonces professionnelles
        VBox annoncesBox = createAnnoncesBox(primaryStage);

        // Conteneur central avec ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(annoncesBox);
        scrollPane.setFitToWidth(true); // Ajuster la largeur du ScrollPane à la largeur de la scène
        scrollPane.setPadding(new Insets(20)); // Ajouter des marges
        scrollPane.setStyle("-fx-background: transparent;"); // Arrière-plan transparent

        // Conteneur principal
        VBox centerBox = new VBox(20);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.getChildren().addAll(titleLabel, scrollPane);
        AnchorPane.setTopAnchor(centerBox, 60.0);
        AnchorPane.setLeftAnchor(centerBox, 0.0);
        AnchorPane.setRightAnchor(centerBox, 0.0);
        AnchorPane.setBottomAnchor(centerBox, 0.0);

        root.getChildren().add(centerBox);

        // Affichage de la scène
        Scene scene = new Scene(root, 3024, 1964);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Liste des propriétés immobilières");
        primaryStage.show();
    }

    private VBox createAnnoncesBox(Stage primaryStage) {
        VBox annoncesBox = new VBox(20);
        annoncesBox.setAlignment(Pos.CENTER);

        // Générer de fausses annonces
        for (int i = 1; i <= 15; i++) {
            String title = "Propriété " + i;
            String details = "5 pièces - 200 m² - Prix: " + (i * 100000) + " €";
            String imagePath = "image2.jpeg"; // L'image peut être la même pour toutes les annonces
            VBox annonce = createAnnonce(title, details, imagePath, primaryStage.getWidth() * 0.75);
            annoncesBox.getChildren().add(annonce);
        }

        // Adjust image width when stage width changes
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            double newWidth = newVal.doubleValue() * 0.75; // 3/4 of the new stage width
            for (int i = 0; i < annoncesBox.getChildren().size(); i++) {
                VBox annonce = (VBox) annoncesBox.getChildren().get(i);
                ((ImageView) ((GridPane) annonce.getChildren().get(0)).getChildren().get(0)).setFitWidth(newWidth);
            }
        });

        return annoncesBox;
    }

    private VBox createAnnonce(String title, String details, String imagePath, double imageWidth) {
        VBox annonceBox = new VBox(10);
        annonceBox.setAlignment(Pos.CENTER_LEFT);

        Image image = new Image(imagePath);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(100);
        imageView.setFitWidth(imageWidth); // Set image width to 3/4 of the stage width
        imageView.setPreserveRatio(true);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(5);
        gridPane.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        Label detailsLabel = new Label(details);
        detailsLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        detailsLabel.setWrapText(true); // Permettre le retour à la ligne pour la description

        Button visitButton = new Button("Programmer une visite");
        visitButton.setFont(Font.font("Arial", FontWeight.NORMAL, 12));

        gridPane.add(imageView, 0, 0);
        gridPane.add(titleLabel, 1, 0);
        gridPane.add(detailsLabel, 1, 1);
        gridPane.add(visitButton, 1, 2); // Ajouter le bouton à la grille

        annonceBox.getChildren().add(gridPane);

        return annonceBox;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
