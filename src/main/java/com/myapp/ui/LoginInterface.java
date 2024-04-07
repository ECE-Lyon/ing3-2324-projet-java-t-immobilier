package com.myapp.ui;


import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.sql.Date;


import java.sql.*;

public class LoginInterface extends Application {

    private Connection connection;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sign Up");

        // Connexion à la base de données
        try {
            connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Conteneur principal avec une grille pour organiser les éléments
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(20);
        root.setVgap(20);
        root.setPadding(new Insets(20));
        // Définir le fond de la scène en blanc
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
        // Image sur la partie gauche de l'écran
        ImageView imageView = new ImageView(new Image("login.png"));
        imageView.setFitWidth(400); // Ajustez la largeur de l'image selon vos besoins
        imageView.setPreserveRatio(true);
        root.add(imageView, 0, 1);
        // Conteneur pour les éléments du formulaire
        VBox formLayout = new VBox();
        formLayout.setAlignment(Pos.CENTER_LEFT);
        formLayout.setSpacing(20);
        // Titre
        Label title = new Label("              Sauvegardez votre compte");
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: #333;");
        // Sous-titre
        Label subtitle = new Label("       Accedez à nos propriétés de rêve. Gratuitement, pour toujours.");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
        // Formulaire d'inscription
        GridPane form = new GridPane();
        form.setAlignment(Pos.CENTER);
        form.setHgap(10);
        form.setVgap(10);


        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        Button signUpButton = new Button("Sign up");
        signUpButton.setStyle("-fx-background-color: #2289dd; -fx-text-fill: white;");
        signUpButton.setMaxWidth(Double.MAX_VALUE);
        Hyperlink loginLink = new Hyperlink("Login");
        loginLink.setTextFill(Color.valueOf("#22DD88"));

        // Ajout des éléments au formulaire
        form.add(emailField, 0, 0);
        form.add(passwordField, 0, 1);
        form.add(signUpButton, 0, 2);
        form.add(loginLink, 0, 3);
        GridPane.setHalignment(signUpButton, HPos.RIGHT);
        GridPane.setHalignment(loginLink, HPos.RIGHT);

        // Ajout des éléments au conteneur du formulaire
        formLayout.getChildren().addAll(title, subtitle, form);
        // Ajout des éléments à la grille principale
        root.add(formLayout, 1, 1);
        // Configuration de la scène
        Scene scene = new Scene(root, 1000, 600); // Taille de la scène ajustée selon vos besoins
        primaryStage.setScene(scene);
        primaryStage.show();

        // Gestionnaire d'événements pour le bouton de connexion
        signUpButton.setOnAction(event -> {
            String email = emailField.getText();
            String password = passwordField.getText();
            try {
                boolean authenticated = verifyConnection(email, password);
                if (authenticated) {
                    Utilisateur user = getUserByEmail(email);
                    Utilisateur.setCurrentUser(user);
                    Stage stage = (Stage) signUpButton.getScene().getWindow();
                    ModernUIApp.launchApp(stage);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Identifiant ou mot de passe incorrect.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Une erreur est survenue lors de la connexion à la base de données.");
            }
        });

        loginLink.setOnAction(event -> {
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Créer un compte");
            dialog.setHeaderText("Entrez votre adresse e-mail et votre mot de passe :");

            // Boutons pour confirmer ou annuler la création de compte
            ButtonType createButtonType = new ButtonType("Créer", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

            // Création des champs de texte pour l'email et le mot de passe
            GridPane dialogContent = new GridPane();
            dialogContent.setHgap(10);
            dialogContent.setVgap(10);
            dialogContent.setPadding(new Insets(20, 150, 10, 10));

            TextField emailDialogField = new TextField();
            emailDialogField.setPromptText("Email");

            PasswordField passwordDialogField = new PasswordField();
            passwordDialogField.setPromptText("Password");

            dialogContent.add(new Label("Email:"), 0, 0);
            dialogContent.add(emailDialogField, 1, 0);
            dialogContent.add(new Label("Mot de passe:"), 0, 1);
            dialogContent.add(passwordDialogField, 1, 1);

            dialog.getDialogPane().setContent(dialogContent);

            // Activation ou désactivation du bouton de création en fonction de la saisie de l'utilisateur
            Button createButton = (Button) dialog.getDialogPane().lookupButton(createButtonType);
            createButton.setDisable(true);

            emailDialogField.textProperty().addListener((observable, oldValue, newValue) -> {
                createButton.setDisable(newValue.trim().isEmpty());
            });

            // Résultat du dialogue lorsque l'utilisateur clique sur "Créer" ou "Annuler"
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == createButtonType) {
                    return new Pair<>(emailDialogField.getText(), passwordDialogField.getText());
                }
                return null;
            });

            // Affichage de la boîte de dialogue et traitement des résultats
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
        });
    }

    private Utilisateur getUserByEmail(String email) throws SQLException {
        String query = "SELECT * FROM UTILISATEUR WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id_user");
                String userEmail = resultSet.getString("email");
                String password = resultSet.getString("password");
                Date inscriptionDate = resultSet.getDate("inscription_date");
                String userName = resultSet.getString("name");
                boolean status = resultSet.getBoolean("status_user");
                return new Utilisateur(id, userEmail, password, inscriptionDate, userName, status);
            } else {
                return null;
            }
        }
    }


    // Méthode pour se connecter à la base de données
    private void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:8889/immobilier_ing3", "root", "root");
    }

    // Méthode pour vérifier l'authentification de l'utilisateur
    private boolean verifyConnection(String email, String password) throws SQLException {
        String query = "SELECT * FROM UTILISATEUR WHERE email = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Renvoie vrai si un utilisateur correspond aux informations fournies
        }
    }

    // Méthode pour créer un utilisateur dans la base de données
    private boolean createUser(String email, String password) throws SQLException {
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

    // Méthode pour afficher une boîte de dialogue d'alerte
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
