package controllers;

import models.Utilisateur;
import dao.DatabaseConnection;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date; // Importer java.sql.Date pour utiliser Date dans la base de données

public class LoginController {

    private Connection connection;

    public LoginController() {
        // Initialisation de la connexion à la base de données
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleSignUp(String email, String password) {
        try {
            boolean authenticated = verifyConnection(email, password);
            if (authenticated) {
                Utilisateur user = getUserByEmail(email);
                Utilisateur.setCurrentUser(user);
                // Remplacez ModernUIApp.launchApp(stage); par le code pour afficher votre interface après la connexion réussie
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Identifiant ou mot de passe incorrect.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Une erreur est survenue lors de la connexion à la base de données.");
        }
    }

    public void handleLoginLink() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Créer un compte");
        dialog.setHeaderText("Entrez votre adresse e-mail et votre mot de passe :");

        // Boutons pour confirmer ou annuler la création de compte
        ButtonType createButtonType = new ButtonType("Créer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        // Création des champs de texte pour l'email et le mot de passe
        TextField emailDialogField = new TextField();
        emailDialogField.setPromptText("Email");

        PasswordField passwordDialogField = new PasswordField();
        passwordDialogField.setPromptText("Password");

        // Code pour la configuration de la boîte de dialogue
        dialog.getDialogPane().setContent(new VBox(8, new Label("Email:"), emailDialogField, new Label("Password:"), passwordDialogField));

        // Activation ou désactivation du bouton de création en fonction de la saisie de l'utilisateur
        Button createButton = (Button) dialog.getDialogPane().lookupButton(createButtonType);
        createButton.setDisable(true);

        emailDialogField.textProperty().addListener((observable, oldValue, newValue) -> {
            createButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                return new Pair<>(emailDialogField.getText(), passwordDialogField.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            String email = result.getKey();
            String password = result.getValue();
            try {
                boolean created = createUser(email, password);
                if (created) {
                    showAlert(Alert.AlertType.INFORMATION, "Création réussie", "Votre compte a été créé avec succès !");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur de création", "Impossible de créer le compte. Veuillez réessayer.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur de création", "Une erreur est survenue lors de la création du compte.");
            }
        });
    }

    public Utilisateur getUserByEmail(String email) throws SQLException {
        String query = "SELECT * FROM UTILISATEUR WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id_user"); // Modifier ici pour utiliser le bon nom de colonne
                String userEmail = resultSet.getString("email");
                String password = resultSet.getString("password");
                Date inscriptionDate = resultSet.getDate("inscription_date");
                String userName = resultSet.getString("name");
                boolean status = resultSet.getBoolean("status_user"); // Modifier ici pour utiliser le bon nom de colonne
                return new Utilisateur(id, userEmail, password, inscriptionDate, userName, status);
            } else {
                return null;
            }
        }
    }

    // Méthode pour vérifier l'authentification de l'utilisateur
    public boolean verifyConnection(String email, String password) throws SQLException {
        String query = "SELECT * FROM UTILISATEUR WHERE email = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Renvoie vrai si un utilisateur correspond aux informations fournies
        }
    }

    // Méthode pour créer un utilisateur dans la base de données
    public boolean createUser(String email, String password) throws SQLException {
        String query = "INSERT INTO UTILISATEUR (email, password, inscription_date, name, status_user) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, password);
            statement.setDate(3, new java.sql.Date(System.currentTimeMillis())); // Date du jour
            statement.setString(4, email); // Nom mis comme l'email
            statement.setBoolean(5, false); // Statut initial à 0
            // Exécution de la requête
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
