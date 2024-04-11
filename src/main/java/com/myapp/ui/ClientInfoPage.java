package com.myapp.ui;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ClientInfoPage extends Application {

    private boolean isEditable = false; // Pour suivre l'état d'édition

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Informations Client");

        // Conteneur principal avec une grille pour organiser les éléments
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(20);
        root.setVgap(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #ffffff;");

        // Logo de l'agence (remplacer "agence_logo.png" par le chemin de votre propre logo)
        ImageView logoView = new ImageView(new Image("ece_immo.jpeg"));
        logoView.setFitWidth(200); // Ajustez la largeur du logo selon vos besoins
        logoView.setPreserveRatio(true);

        // Centrer le logo horizontalement
        HBox logoContainer = new HBox();
        logoContainer.getChildren().add(logoView);
        logoContainer.setAlignment(Pos.CENTER); // Centrer horizontalement

        // Ajouter le logo au conteneur principal
        root.add(logoContainer, 0, 0, 2, 1); // Logo prendra 2 colonnes, centré horizontalement

        // Image à gauche du formulaire (remplacer "left_image.png" par le chemin de votre propre image)
        ImageView leftImageView = new ImageView(new Image("info_icon.png"));
        leftImageView.setFitHeight(300); // Ajustez la hauteur de l'image selon vos besoins
        leftImageView.setPreserveRatio(true);

        // Formulaire pour les informations du client
        VBox clientInfoLayout = new VBox();
        clientInfoLayout.setAlignment(Pos.CENTER_LEFT);
        clientInfoLayout.setSpacing(10);
        clientInfoLayout.setPadding(new Insets(0, 20, 0, 20)); // Ajustez les marges du formulaire

        // Labels et champs de texte pour les informations du client
        Label nameLabel = new Label("Nom:");
        TextField nameField = new TextField("John"); // Champ de texte pour le nom
        nameField.setEditable(isEditable);

        Label surnameLabel = new Label("Prénom:");
        TextField surnameField = new TextField("Doe"); // Champ de texte pour le prénom
        surnameField.setEditable(isEditable);

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField("john.doe@example.com"); // Champ de texte pour l'email
        emailField.setEditable(isEditable);

        Label passwordLabel = new Label("Mot de passe:");
        TextField passwordField = new TextField("motdepasse123"); // Champ de texte pour le mot de passe
        passwordField.setEditable(isEditable);

        Label addressLabel = new Label("Adresse:");
        TextField addressField = new TextField("123 rue de la Rue, Ville"); // Champ de texte pour l'adresse
        addressField.setEditable(isEditable);

        // Bouton pour modifier ou enregistrer les modifications
        Button editButton = new Button("Modifier");
        editButton.setStyle("-fx-background-color: rgb(213, 119, 195); -fx-text-fill: white;");
        editButton.setMaxWidth(Double.MAX_VALUE);
        editButton.setPrefHeight(50); // Hauteur du bouton ajustée selon vos besoins
        editButton.setFont(Font.font(16)); // Taille de police du bouton ajustée selon vos besoins

        // Action lorsque le bouton est cliqué
        editButton.setOnAction(e -> {
            isEditable = !isEditable; // Inverser l'état d'édition
            if (isEditable) {
                editButton.setText("Enregistrer");
            } else {
                editButton.setText("Modifier");
                // Afficher une fenêtre d'information lorsque le profil est mis à jour
                //showAlert("Profil mis à jour avec succès !");
            }
            // Activer ou désactiver l'édition des champs de texte
            nameField.setEditable(isEditable);
            surnameField.setEditable(isEditable);
            emailField.setEditable(isEditable);
            passwordField.setEditable(isEditable);
            addressField.setEditable(isEditable);
        });

        // Ajout des éléments au conteneur des informations du client
        clientInfoLayout.getChildren().addAll(
                nameLabel, nameField,
                surnameLabel, surnameField,
                emailLabel, emailField,
                passwordLabel, passwordField,
                addressLabel, addressField,
                editButton
        );

        // Ajout des éléments à la grille principale
        root.add(leftImageView, 0, 1);
        root.add(clientInfoLayout, 1, 1);

        // Configuration de la scène
        Scene scene = new Scene(root, 1000, 600); // Taille de la scène ajustée selon vos besoins
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
