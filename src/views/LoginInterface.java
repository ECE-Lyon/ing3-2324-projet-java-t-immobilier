package views;

import models.Utilisateur;
import controllers.LoginController;
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

import java.sql.SQLException;

public class LoginInterface extends Application {

    private LoginController loginController;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sign Up");

        // Création du contrôleur
        loginController = new LoginController();

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

        // Associer les événements aux actions du contrôleur
        signUpButton.setOnAction(event -> {
            String email = emailField.getText();
            String password = passwordField.getText();
            try {
                boolean authenticated = loginController.verifyConnection(email, password);
                if (authenticated) {
                    Utilisateur user = loginController.getUserByEmail(email);
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
                    boolean created = loginController.createUser(email, password);
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

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
