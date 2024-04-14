package com.myapp.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.*;

public class ClientInfoPage extends Application {

    private boolean isEditable = false; // Pour suivre l'état d'édition

    private int userId; // Ajout de l'ID de l'utilisateur

    private TextField nameField;
    private TextField emailField;
    private TextField passwordField;
    private TextField dateField;
    private TextField statutField;

    public ClientInfoPage(int userId) {
        this.userId = userId;
    }

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
        nameField = new TextField();
        nameField.setEditable(false); // Le champ de texte est désactivé par défaut

        Label emailLabel = new Label("Email:");
        emailField = new TextField();
        emailField.setEditable(false); // Le champ de texte est désactivé par défaut

        Label passwordLabel = new Label("Mot de passe:");
        passwordField = new TextField();
        passwordField.setEditable(false); // Le champ de texte est désactivé par défaut

        Label dateLabel = new Label("Inscription Date:");
        dateField = new TextField();
        dateField.setEditable(false); // Le champ de texte est désactivé par défaut

        Label statutLabel = new Label("Statut:");
        statutField = new TextField();
        statutField.setEditable(false); // Le champ de texte est désactivé par défaut

        // Bouton pour modifier ou enregistrer les modifications
        Button editButton = new Button("Modifier");
        editButton.setStyle("-fx-background-color: rgb(213, 119, 195); -fx-text-fill: white;");
        editButton.setMaxWidth(Double.MAX_VALUE);
        editButton.setPrefHeight(50); // Hauteur du bouton ajustée selon vos besoins
        editButton.setFont(Font.font(16)); // Taille de police du bouton ajustée selon vos besoins

        // Action lorsque le bouton est cliqué
        editButton.setOnAction(e -> {
            if (isEditable) {
                // Enregistrer les modifications dans la base de données
                updateClientInfo();
                showAlert("Les modifications ont été enregistrées avec succès !");
                editButton.setText("Modifier");
            } else {
                editButton.setText("Enregistrer");
            }
            isEditable = !isEditable; // Inverser l'état d'édition
            // Activer ou désactiver l'édition des champs de texte
            nameField.setEditable(isEditable);
            emailField.setEditable(isEditable);
            passwordField.setEditable(isEditable);
            dateField.setEditable(isEditable);
            statutField.setEditable(isEditable);
        });

        // Ajout des éléments au conteneur des informations du client
        clientInfoLayout.getChildren().addAll(
                nameLabel, nameField,
                emailLabel, emailField,
                passwordLabel, passwordField,
                dateLabel, dateField,
                statutLabel, statutField,
                editButton
        );

        // Ajout des éléments à la grille principale
        root.add(leftImageView, 0, 1);
        root.add(clientInfoLayout, 1, 1);

        // Configuration de la scène
        Scene scene = new Scene(root, 1000, 600); // Taille de la scène ajustée selon vos besoins
        primaryStage.setScene(scene);
        primaryStage.show();

        // Récupérer les informations du client à partir de la base de données et les remplir dans les champs de texte
        populateClientInfo();
    }

    private void populateClientInfo() {
        try {
            // Connexion à la base de données
            Connection connection = DatabaseConnection.getConnection();
            // Requête SQL pour récupérer les informations de l'utilisateur à partir de l'ID
            String query = "SELECT name, email, password, inscription_date, status_user FROM UTILISATEUR WHERE id_user = ?";

            // Préparation de la requête
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);

            // Exécution de la requête
            ResultSet resultSet = preparedStatement.executeQuery();

            // Traitement des résultats de la requête
            if (resultSet.next()) {
                // Remplissage des champs de texte avec les données récupérées
                nameField.setText(resultSet.getString("name"));
                emailField.setText(resultSet.getString("email"));
                passwordField.setText(resultSet.getString("password"));
                dateField.setText(resultSet.getString("inscription_date"));
                statutField.setText(resultSet.getBoolean("status_user") ? "Client" : "Employé");
            }

            // Fermeture des ressources
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour mettre à jour les informations du client dans la base de données
    private void updateClientInfo() {
        try {
            // Connexion à la base de données
            Connection connection = DatabaseConnection.getConnection();

            // Requête SQL pour mettre à jour les informations du client
            String query = "UPDATE UTILISATEUR SET name = ?, email = ?, password = ?, inscription_date = ?, status_user = ? WHERE id_user = ?";

            // Préparation de la requête
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nameField.getText());
            preparedStatement.setString(2, emailField.getText());
            preparedStatement.setString(3, passwordField.getText());
            preparedStatement.setString(4, dateField.getText());
            preparedStatement.setBoolean(5, statutField.getText().equalsIgnoreCase("Client")); // Convertir le texte en booléen
            preparedStatement.setInt(6, userId);

            // Exécution de la requête
            preparedStatement.executeUpdate();

            // Fermeture des ressources
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Méthode pour afficher une alerte (pop-up) avec un message spécifié
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
