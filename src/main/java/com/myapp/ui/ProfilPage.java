package com.myapp.ui;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Date;

public class ProfilPage extends Application {

    private String userName;
    private String userEmail;
    private Date inscriptionDate;
    private boolean userStatus;
    private Button confirmButton; // Déclaration du bouton de confirmation en tant que membre de classe

    public ProfilPage(String userName, String userEmail, Date inscriptionDate, boolean userStatus) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.inscriptionDate = inscriptionDate;
        this.userStatus = userStatus;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Profil Utilisateur");

        // Création des éléments de la page de profil
        Label titleLabel = new Label("Profil Utilisateur");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label usernameLabel = new Label("Nom d'utilisateur :");
        TextField usernameField = new TextField(userName);
        usernameField.setEditable(false);

        Label emailLabel = new Label("Email :");
        TextField emailField = new TextField(userEmail);
        emailField.setEditable(false);

        Label dateLabel = new Label("Date d'inscription : " + inscriptionDate);
        Label roleLabel = new Label("Rôle : " + (userStatus ? "Administrateur" : "Utilisateur standard"));

        // Bouton de modification
        Button editButton = new Button("Modifier");
        editButton.setOnAction(event -> {
            // Activer l'édition des champs de texte et afficher le bouton "Confirmer"
            usernameField.setEditable(true);
            emailField.setEditable(true);
            editButton.setVisible(false);
            confirmButton.setVisible(true);
        });

        // Bouton de confirmation des modifications
        confirmButton = new Button("Confirmer"); // Initialisation du bouton de confirmation
        confirmButton.setVisible(false);
        confirmButton.setOnAction(event -> {
            // Enregistrer les modifications dans la base de données
            String newUsername = usernameField.getText();
            String newEmail = emailField.getText();

            // Mettre à jour les données dans la classe Utilisateur
            Utilisateur user = Utilisateur.getCurrentUser();
            user.setName(newUsername);
            user.setEmail(newEmail);

            try {
                // Mettre à jour les informations de l'utilisateur dans la base de données
                DatabaseManager.updateUser(user);
            } catch (SQLException e) {
                e.printStackTrace(); // Gérer les erreurs de mise à jour de la base de données
            }

            // Désactiver l'édition des champs de texte et masquer le bouton "Confirmer"
            usernameField.setEditable(false);
            emailField.setEditable(false);
            editButton.setVisible(true);
            confirmButton.setVisible(false);
        });


        // Bouton de déconnexion
        Button logoutButton = new Button("Déconnexion");
        logoutButton.setOnAction(event -> {
            // Action de déconnexion ici
            primaryStage.close(); // Ferme la page de profil
        });

        // Mise en page des éléments
        VBox profileLayout = new VBox(20);
        profileLayout.setAlignment(Pos.CENTER_LEFT);
        profileLayout.getChildren().addAll(
                titleLabel,
                new HBox(10, usernameLabel, usernameField),
                new HBox(10, emailLabel, emailField),
                dateLabel,
                roleLabel,
                new HBox(10, editButton, confirmButton),
                logoutButton
        );

        // Création de la scène
        Scene scene = new Scene(profileLayout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Méthode pour afficher la page de profil
    public void showProfile() {
        launch();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
