package views;


import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Test extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Créer un compte");

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
        ImageView imageView = new ImageView(new Image("login2.png"));
        imageView.setFitWidth(400); // Ajustez la largeur de l'image selon vos besoins
        imageView.setPreserveRatio(true);
        root.add(imageView, 0, 1);

        // Conteneur pour les éléments du formulaire
        VBox formLayout = new VBox();
        formLayout.setAlignment(Pos.CENTER_LEFT);
        formLayout.setSpacing(20);

        // Titre
        Label title = new Label("                   Créez votre compte");
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: #333;");

        // Sous-titre
        Label subtitle = new Label("       Accedez à nos propriétés de rêve. Gratuitement, pour toujours.");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");

        // Formulaire de création de compte
        GridPane form = new GridPane();
        form.setAlignment(Pos.CENTER);
        form.setHgap(10);
        form.setVgap(10);

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");

        Button createAccountButton = new Button("Créer un compte");
        createAccountButton.setStyle("-fx-background-color: rgb(213, 119, 195); -fx-text-fill: white;");
        createAccountButton.setMaxWidth(Double.MAX_VALUE);

        // Ajout des éléments au formulaire
        form.add(emailField, 0, 0);
        form.add(passwordField, 0, 1);
        form.add(createAccountButton, 0, 2);
        GridPane.setHalignment(createAccountButton, HPos.RIGHT);

        // Ajout des éléments au conteneur du formulaire
        formLayout.getChildren().addAll(title, subtitle, form);

        // Ajout des éléments à la grille principale
        root.add(formLayout, 1, 1);

        // Configuration de la scène
        Scene scene = new Scene(root, 1000, 600); // Taille de la scène ajustée selon vos besoins
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
