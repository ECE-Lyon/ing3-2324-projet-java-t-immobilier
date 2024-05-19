package views;

import dao.VisitDAO;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import models.Visit;

import java.util.List;

public class VisitesInterfaceEmployee extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Mes visites");
        primaryStage.setFullScreen(true); // Mettre la fenêtre en plein écran

        // Conteneur principal avec une grille pour organiser les éléments
        GridPane root = new GridPane();
        root.setAlignment(Pos.TOP_CENTER);
        root.setHgap(20);
        root.setVgap(20);
        root.setPadding(new Insets(20));
        root.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        // Logo de l'agence en haut de la page
        ImageView logoView = new ImageView(new Image("ece_immo.jpeg"));
        logoView.setFitWidth(200); // Ajustez la largeur du logo selon vos besoins
        logoView.setPreserveRatio(true);

        // Centrer le logo horizontalement
        HBox logoContainer = new HBox();
        logoContainer.getChildren().add(logoView);
        logoContainer.setAlignment(Pos.CENTER); // Centrer horizontalement

        // Titre de la page
        Label title = new Label("Mes visites");
        title.setFont(Font.font("Arial", 28));
        title.setTextFill(Color.valueOf("#333333"));

        // Conteneur pour les visites avec une barre de défilement
        ScrollPane scrollPane = new ScrollPane();
        VBox visitsContainer = new VBox();
        visitsContainer.setAlignment(Pos.TOP_CENTER);
        visitsContainer.setSpacing(20);
        visitsContainer.setPadding(new Insets(20));
        scrollPane.setContent(visitsContainer);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true); // Permet au contenu de s'adapter à la largeur du ScrollPane
        scrollPane.setPrefHeight(600); // Ajustez la hauteur selon vos besoins
        scrollPane.setPrefWidth(800); // Ajustez la largeur selon vos besoins
        scrollPane.setStyle("-fx-background-color: transparent;");

        // Récupérer toutes les visites depuis la base de données
        List<Visit> visits = VisitDAO.getAllVisits();
        if (visits.isEmpty()) {
            Label noVisitsLabel = new Label("Aucune visite programmée.");
            noVisitsLabel.setFont(Font.font("Arial", 16));
            visitsContainer.getChildren().add(noVisitsLabel);
        } else {
            for (Visit visit : visits) {
                // Créer un conteneur VBox pour afficher les détails de la visite
                VBox visitDetails = new VBox();
                visitDetails.setSpacing(10); // Espacement entre les détails de la visite
                visitDetails.setPadding(new Insets(10));
                visitDetails.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1px; -fx-background-color: #f9f9f9;");

                visitDetails.getChildren().addAll(
                        new Label("Date: " + visit.getDateVisit()),
                        new Label("Heure: " + visit.getTimeVisit()),
                        new Label("Feedback: " + (visit.getFeedback() != null ? visit.getFeedback() : "Aucun feedback")),
                        new Label("ID de la propriété: " + visit.getIdProperty()),
                        new Label("ID de l'utilisateur: " + visit.getIdUser())
                );
                visitsContainer.getChildren().add(visitDetails);
            }
        }

        // Ajout des éléments à la grille principale
        root.add(logoContainer, 0, 0);
        root.add(title, 0, 1);
        root.add(scrollPane, 0, 2);

        // Configuration de la scène
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}