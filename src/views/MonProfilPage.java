package views;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import models.Utilisateur;



public class MonProfilPage extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Mon Profil");

        Utilisateur user = Utilisateur.getCurrentUser();
        int userId = user.getId();

        // Conteneur principal avec une grille pour organiser les éléments
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(20);
        root.setVgap(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #FFFFFF;");

        // Logo de l'agence en haut de la page
        ImageView logoView = new ImageView(new Image("ece_immo.jpeg"));
        logoView.setFitWidth(200); // Ajustez la largeur du logo selon vos besoins
        logoView.setPreserveRatio(true);

        // Centrer le logo horizontalement
        HBox logoContainer = new HBox();
        logoContainer.getChildren().add(logoView);
        logoContainer.setAlignment(Pos.CENTER); // Centrer horizontalement

        // Placez le logo dans la première colonne et la première ligne
        root.add(logoContainer, 0, 0, 2, 1); // Le logo prendra 2 colonnes, centré horizontalement

        // Conteneur pour les informations du profil
        VBox profileLayout = new VBox();
        profileLayout.setAlignment(Pos.CENTER_LEFT);
        profileLayout.setSpacing(20);

        // Titre
        Label title = new Label("            Mon Profil");
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: #333;");

        // Sous-titre
        Label subtitle = new Label("           Informations sur votre profil");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");

        boolean userStatus = getCurrentUserStatut();



        // Bouton pour afficher les informations du client
        Button myInfoButton = createSectionButton("Mes informations");
        myInfoButton.setOnAction(event -> {
            Stage clientInfoStage = new Stage();
            ClientInfoPage clientInfoPage = new ClientInfoPage(userId);
            clientInfoPage.start(clientInfoStage);
        });
        if(!userStatus){
            Button myVisitsButton = createSectionButton("Mes visites");
            myVisitsButton.setOnAction(event -> {
                Stage mesVisitesStage = new Stage();
                MesVisitesInterface mesVisitesInterface = new MesVisitesInterface();
                mesVisitesInterface.start(mesVisitesStage);
            });

            // Création du bouton pour l'historique
            Button myHistoryButton = createSectionButton("Mon historique");
            myHistoryButton.setOnAction(event -> {
                Stage historiqueStage = new Stage();
                HistoriquePage historiquePage = new HistoriquePage(user);
                historiquePage.start(historiqueStage);
            });

            // Ajout des éléments au conteneur du profil
            profileLayout.getChildren().addAll(title, subtitle, myInfoButton, myVisitsButton, myHistoryButton);

        }
        else{

            Button theVisitsButton = createSectionButton("Les visites programmées");
            theVisitsButton.setOnAction(event -> {
                Stage mesVisitesStage = new Stage();
                VisitesInterfaceEmployee visitesInterfaceEmployee = new VisitesInterfaceEmployee();
                visitesInterfaceEmployee.start(mesVisitesStage);
            });
            // Ajout des éléments au conteneur du profil
            profileLayout.getChildren().addAll(title, subtitle, myInfoButton,theVisitsButton);
        }

        // Ajout des éléments à la grille principale
        root.add(profileLayout, 0, 1, 2, 1);

        // Configuration de la scène
        Scene scene = new Scene(root, 1920, 1080); // Taille de la scène ajustée selon vos besoins
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Fonction pour créer des boutons de section
    // Fonction pour créer des boutons de section
    private Button createSectionButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: rgb(213, 119, 195); -fx-text-fill: white;");
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(50); // Hauteur du bouton ajustée selon vos besoins
        button.setFont(Font.font(16)); // Taille de police du bouton ajustée selon vos besoins
        return button;
    }

    // Méthode pour obtenir le statut de l'utilisateur actuellement connecté
    public boolean getCurrentUserStatut() {
        if (Utilisateur.getCurrentUser() != null) {
            return Utilisateur.getCurrentUser().getStatus();
        } else {
            return false; // Retourner false si aucun utilisateur n'est connecté
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}