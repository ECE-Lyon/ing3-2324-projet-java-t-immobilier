package views;

import dao.DatabaseConnection;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Utilisateur;
import javafx.animation.TranslateTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MesVisitesInterface extends Application {

    // Méthode pour récupérer les visites de l'utilisateur courant depuis la base de données
    private ResultSet getVisitsForCurrentUser() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DatabaseConnection.getConnection();
            String query = "SELECT V.*, P.id_property " +
                    "FROM VISIT V " +
                    "JOIN PROPERTY P ON V.id_property = P.id_property " +
                    "WHERE P.id_client IN (SELECT id_client FROM CLIENT WHERE id_user = ?)";
            statement = connection.prepareStatement(query);
            statement.setInt(1, Utilisateur.getCurrentUser().getId());
            resultSet = statement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            // Gérer l'exception ici ou la propager
            throw e;
        }
    }


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
        scrollPane.setContent(visitsContainer);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent;");

        try (ResultSet resultSet = getVisitsForCurrentUser()) {
            boolean hasVisits = false;
            while (resultSet.next()) {
                hasVisits = true;
                String date = resultSet.getString("date_visit");
                String time = resultSet.getString("time_visit");
                String feedback = resultSet.getString("feedback");
                int propertyId = resultSet.getInt("id_property");

                // Récupérer les détails de la propriété associée à cette visite
                String propertyDetails = getPropertyDetails(propertyId);

                // Créer un conteneur VBox pour afficher les détails de la visite
                VBox visitDetails = new VBox();
                visitDetails.getChildren().addAll(
                        new Label("Date: " + date),
                        new Label("Heure: " + time),
                        new Label("Feedback: " + feedback),
                        new Label("Détails de la propriété: " + propertyDetails)
                );
                visitsContainer.getChildren().add(visitDetails);
            }
            if (!hasVisits) {
                Label noVisitsLabel = new Label("Aucune visite programmée.");
                noVisitsLabel.setFont(Font.font("Arial", 16));
                visitsContainer.getChildren().add(noVisitsLabel);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Gestion de l'erreur de base de données
        }

        // Ajout des éléments à la grille principale
        root.add(logoContainer, 0, 0);
        root.add(title, 0, 1);
        root.add(scrollPane, 0, 2);

        // Configuration de la scène
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Ajouter des animations de défilement entre les visites
        addAnimations(visitsContainer);
    }

    private String getPropertyDetails(int propertyId) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String propertyDetails = "";

        try {
            connection = DatabaseConnection.getConnection();
            String query = "SELECT * FROM PROPERTY WHERE id_property = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, propertyId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Récupérer les détails de la propriété
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                boolean hasPool = resultSet.getBoolean("has_pool");
                boolean hasGarden = resultSet.getBoolean("has_garden");
                int nbRooms = resultSet.getInt("nb_room");

                // Construire une chaîne de caractères pour afficher les détails
                propertyDetails = "Description: " + description + "\n" +
                        "Prix: " + price + "\n" +
                        "Piscine: " + (hasPool ? "Oui" : "Non") + "\n" +
                        "Jardin: " + (hasGarden ? "Oui" : "Non") + "\n" +
                        "Nombre de pièces: " + nbRooms;
            }
        } catch (SQLException e) {
            // Gérer l'exception ici ou la propager
            throw e;
        } finally {
            // Fermer la connexion, le statement et le resultSet
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return propertyDetails;
    }


    // Créer un conteneur pour une visite avec une mise en forme spécifique
    private VBox createVisit(String date, String day, String property, String imageUrl, String description) {
        VBox visitContainer = new VBox();
        visitContainer.setAlignment(Pos.CENTER_LEFT);
        visitContainer.setSpacing(10);
        visitContainer.setPadding(new Insets(10));
        visitContainer.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ccc; -fx-border-width: 1px;");

        Label dateLabel = new Label(date);
        dateLabel.setFont(Font.font("Arial", 16));
        dateLabel.setTextFill(Color.valueOf("#333333"));

        Label dayLabel = new Label(day);
        dayLabel.setFont(Font.font("Arial", 14));
        dayLabel.setTextFill(Color.valueOf("#666666"));

        Label propertyLabel = new Label("Propriété : " + property);
        propertyLabel.setFont(Font.font("Arial", 16));
        propertyLabel.setTextFill(Color.valueOf("#555555"));

        ImageView imageView = new ImageView(new Image(imageUrl));
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(800); // Largeur de l'image fixe
        imageView.setFitHeight(600); // Hauteur de l'image fixe

        Text descriptionText = new Text(description);
        descriptionText.setFont(Font.font("Arial", 14));
        descriptionText.setFill(Color.valueOf("#777777"));
        descriptionText.setWrappingWidth(imageView.getFitWidth() - 20); // Largeur de la description
        descriptionText.setLineSpacing(2); // Espacement entre les lignes

        // Boutons
        Button voirDetailButton = new Button("Voir détail de la visite");
        voirDetailButton.setStyle("-fx-background-color: rgb(213, 119, 195); -fx-text-fill: white;");
        Button ajouterCalendrierButton = new Button("Ajouter au calendrier");
        ajouterCalendrierButton.setStyle("-fx-background-color: rgb(213, 119, 195); -fx-text-fill: white;");

        // Ajout des boutons à une boîte horizontale
        HBox buttonsBox = new HBox(10);
        buttonsBox.setAlignment(Pos.CENTER_LEFT);
        buttonsBox.getChildren().addAll(voirDetailButton, ajouterCalendrierButton);

        // Animation de zoom sur survol
        ScaleTransition scaleIn = new ScaleTransition(Duration.seconds(0.2), imageView);
        scaleIn.setFromX(20);
        scaleIn.setFromY(1);
        scaleIn.setToX(1.1);
        scaleIn.setToY(1.1);

        ScaleTransition scaleOut = new ScaleTransition(Duration.seconds(0.2), imageView);
        scaleOut.setFromX(1.1);
        scaleOut.setFromY(1.1);
        scaleOut.setToX(1);
        scaleOut.setToY(1);

        visitContainer.setOnMouseEntered(event -> scaleIn.play());
        visitContainer.setOnMouseExited(event -> scaleOut.play());

        HBox contentBox = new HBox();
        contentBox.setAlignment(Pos.CENTER_LEFT);
        contentBox.setSpacing(10);
        contentBox.getChildren().addAll(imageView, descriptionText);

        visitContainer.getChildren().addAll(dateLabel, dayLabel, propertyLabel, contentBox, buttonsBox);

        return visitContainer;
    }

    // Ajouter des animations de défilement entre les visites
    private void addAnimations(VBox visitsContainer) {
        for (int i = 0; i < visitsContainer.getChildren().size(); i++) {
            Node node = visitsContainer.getChildren().get(i);
            if (node instanceof VBox) {
                VBox visit = (VBox) node;
                TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), visit);
                visit.setOnMouseEntered(event -> {
                    transition.setToX(10);
                    transition.play();
                });
                visit.setOnMouseExited(event -> {
                    transition.setToX(0);
                    transition.play();
                });
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
