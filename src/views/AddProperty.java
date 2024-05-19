package views;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import dao.AddPropertyDAO;

public class AddProperty extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Ajouter une Propriété");

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

        // Conteneur pour les informations de la propriété
        VBox propertyLayout = new VBox();
        propertyLayout.setAlignment(Pos.CENTER_LEFT);
        propertyLayout.setSpacing(20);

        // Titre
        Label title = new Label("Ajouter une Propriété");
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: #333;");

        // Champs de saisie
        TextField streetNumberField = new TextField();
        streetNumberField.setPromptText("Numéro de Rue");

        TextField streetNameField = new TextField();
        streetNameField.setPromptText("Nom de Rue");

        TextField postalCodeField = new TextField();
        postalCodeField.setPromptText("Code Postal");

        TextField cityField = new TextField();
        cityField.setPromptText("Ville");

        TextField priceField = new TextField();
        priceField.setPromptText("Prix");

        TextField sizeField = new TextField();
        sizeField.setPromptText("Superficie (m²)");

        TextField roomsField = new TextField();
        roomsField.setPromptText("Nombre de Chambres");

        CheckBox gardenCheckBox = new CheckBox("Jardin");
        CheckBox poolCheckBox = new CheckBox("Piscine");

        ToggleGroup typeGroup = new ToggleGroup();
        RadioButton houseRadio = new RadioButton("Maison");
        houseRadio.setToggleGroup(typeGroup);
        RadioButton apartmentRadio = new RadioButton("Appartement");
        apartmentRadio.setToggleGroup(typeGroup);

        HBox typeContainer = new HBox(10, houseRadio, apartmentRadio);

        // Bouton publier
        Button publishButton = new Button("Publier");
        publishButton.setStyle("-fx-background-color: rgb(213, 119, 195); -fx-text-fill: white;");
        publishButton.setFont(Font.font(16));

        // Ajout des éléments au conteneur de la propriété
        propertyLayout.getChildren().addAll(
                title,
                createLabeledField("Numéro de Rue:", streetNumberField),
                createLabeledField("Nom de Rue:", streetNameField),
                createLabeledField("Code Postal:", postalCodeField),
                createLabeledField("Ville:", cityField),
                createLabeledField("Prix:", priceField),
                createLabeledField("Superficie (m²):", sizeField),
                createLabeledField("Nombre de Chambres:", roomsField),
                createLabeledField("Jardin:", gardenCheckBox),
                createLabeledField("Piscine:", poolCheckBox),
                createLabeledField("Type:", typeContainer),
                publishButton
        );

        // Ajout des éléments à la grille principale
        root.add(propertyLayout, 0, 1, 2, 1);

        // Configuration de la scène
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Gestionnaire d'événements pour le bouton publier
        publishButton.setOnAction(event -> {
            try {
                AddPropertyDAO propertyDAO = new AddPropertyDAO();
                boolean success = propertyDAO.addProperty(
                        streetNumberField.getText(),
                        streetNameField.getText(),
                        cityField.getText(),
                        postalCodeField.getText(),
                        Double.parseDouble(sizeField.getText()),
                        "Description ici", // Vous devrez ajouter un champ pour la description
                        Double.parseDouble(priceField.getText()),
                        0, // status_sold
                        poolCheckBox.isSelected(),
                        gardenCheckBox.isSelected(),
                        ((RadioButton) typeGroup.getSelectedToggle()).getText(),
                        Integer.parseInt(roomsField.getText()),
                        0, // program_visit
                        1, // id_client
                        1  // id_employee
                );

                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Propriété ajoutée avec succès !");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors de l'ajout de la propriété.");
                    alert.showAndWait();
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez entrer des valeurs numériques valides.");
                alert.showAndWait();
            }
        });
    }

    // Fonction pour créer des champs de saisie avec une étiquette
    private HBox createLabeledField(String labelText, Node field) {
        Label label = new Label(labelText);
        label.setFont(Font.font(14));
        HBox container = new HBox(10);
        container.setAlignment(Pos.CENTER_LEFT);
        container.getChildren().addAll(label, field);
        return container;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
