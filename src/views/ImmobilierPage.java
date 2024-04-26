
package views;


import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ImmobilierPage extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Propriétés");
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

        // Titre de la page avec le bouton "Afficher les filtres"
        Label title = new Label("");
        title.setFont(Font.font("Arial", 28));
        title.setTextFill(Color.valueOf("#333333"));

        Button filtersButton = new Button("Afficher les filtres");
        filtersButton.setStyle("-fx-background-color: rgb(213, 119, 195); -fx-text-fill: white; -fx-font-weight: bold;");
        addHoverAnimation(filtersButton); // Ajouter une animation au survol
        filtersButton.setOnAction(event -> showFiltersPopup(primaryStage)); // Afficher le pop-up des filtres lors du clic
        HBox titleContainer = new HBox(title, filtersButton);
        titleContainer.setAlignment(Pos.CENTER_RIGHT);
        titleContainer.setSpacing(10);

        // Conteneur pour les propriétés avec une barre de défilement
        ScrollPane scrollPane = new ScrollPane();
        VBox propertiesContainer = new VBox();
        propertiesContainer.setAlignment(Pos.TOP_CENTER);
        propertiesContainer.setSpacing(20);
        scrollPane.setContent(propertiesContainer);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent;");

        // Exemple de propriétés (à remplacer par des données dynamiques)
        for (int i = 0; i < 10; i++) {
            VBox property = createProperty("Villa Vue Mer Collines Cannes", "$2,500,000", "Cannes, France", "image1.jpg", "Dans les douces collines de Cannes, surplombant la Méditerranée, se dresse majestueusement une villa de luxe, symbole d'opulence et de raffinement. Les portes en bois massif s'ouvrent sur un hall d'entrée orné de marbre italien, où la lumière naturelle danse à travers des vitraux colorés. Un escalier en spirale, avec une rampe en fer forgé finement ciselée, mène aux étages supérieurs. Les pièces sont des œuvres d'art en elles-mêmes, avec des plafonds voûtés ornés de fresques murales, des lustres en cristal étincelants et des sols en parquet de chêne poli à la perfection... Les fenêtres panoramiques offrent des vues imprenables sur la mer scintillante et les jardins luxuriants qui entourent la propriété...");
            property.prefWidthProperty().bind(root.widthProperty().subtract(40)); // Largeur relative au GridPane
            propertiesContainer.getChildren().add(property);
        }

        // Ajout des éléments à la grille principale
        root.add(logoContainer, 0, 0);
        root.add(titleContainer, 0, 1);
        root.add(scrollPane, 0, 2);

        // Configuration de la scène
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Créer un conteneur pour une propriété avec une mise en forme spécifique
    private VBox createProperty(String name, String price, String location, String imageUrl, String description) {
        VBox propertyContainer = new VBox();
        propertyContainer.setAlignment(Pos.CENTER_LEFT);
        propertyContainer.setSpacing(10);
        propertyContainer.setPadding(new Insets(10));
        propertyContainer.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ccc; -fx-border-width: 1px;");

        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Arial", 16));
        nameLabel.setTextFill(Color.valueOf("#333333"));

        Label priceLabel = new Label("Prix : " + price);
        priceLabel.setFont(Font.font("Arial", 14));
        priceLabel.setTextFill(Color.valueOf("#666666"));

        Label locationLabel = new Label("Emplacement : " + location);
        locationLabel.setFont(Font.font("Arial", 14));
        locationLabel.setTextFill(Color.valueOf("#666666"));

        ImageView imageView = new ImageView(new Image(imageUrl));
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(800); // Largeur de l'image fixe
        imageView.setFitHeight(600); // Hauteur de l'image fixe

        // Bouton "Réserver une visite" au centre de l'image
        Button viewMoreButton = new Button("Réserver une visite");
        viewMoreButton.setStyle("-fx-background-color: rgb(213, 119, 195); -fx-text-fill: white; -fx-font-weight: bold;");
        addHoverAnimation(viewMoreButton); // Ajouter une animation au survol
        VBox.setMargin(viewMoreButton, new Insets(0, 0, 20, 0)); // Marge inférieure pour l'espacement
        VBox imageContainer = new VBox(imageView, viewMoreButton);
        imageContainer.setAlignment(Pos.CENTER);
        imageContainer.setSpacing(10);

        Text descriptionText = new Text(description);
        descriptionText.setFont(Font.font("Arial", 14));
        descriptionText.setFill(Color.valueOf("#777777"));
        descriptionText.setWrappingWidth(imageView.getFitWidth() - 20); // Largeur de la description
        descriptionText.setLineSpacing(2); // Espacement entre les lignes

        // Animation de translation sur survol
        TranslateTransition translateIn = new TranslateTransition(Duration.seconds(0.4), propertyContainer);
        translateIn.setToX(20);

        TranslateTransition translateOut = new TranslateTransition(Duration.seconds(0.4), propertyContainer);
        translateOut.setToX(0);

        propertyContainer.setOnMouseEntered(event -> translateIn.play());
        propertyContainer.setOnMouseExited(event -> translateOut.play());

        HBox contentBox = new HBox();
        contentBox.setAlignment(Pos.CENTER_LEFT);
        contentBox.setSpacing(10);
        contentBox.getChildren().addAll(imageContainer, descriptionText);

        // Ajouter les éléments au conteneur de propriété
        propertyContainer.getChildren().addAll(nameLabel, priceLabel, locationLabel, contentBox);

        return propertyContainer;
    }

    // Ajouter une animation de mise à l'échelle sur le bouton lorsqu'il est survolé
    private void addHoverAnimation(Button button) {
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(100), button);
        scaleIn.setToX(1.06); // Augmenter la valeur pour un agrandissement plus prononcé
        scaleIn.setToY(1.06); // Augmenter la valeur pour un agrandissement plus prononcé

        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(100), button);
        scaleOut.setToX(1);
        scaleOut.setToY(1);

        button.setOnMouseEntered(event -> scaleIn.play());
        button.setOnMouseExited(event -> scaleOut.play());
    }

    // Afficher le pop-up des filtres
    private void showFiltersPopup(Stage primaryStage) {
        Stage filtersStage = new Stage();
        filtersStage.initModality(Modality.WINDOW_MODAL);
        filtersStage.initOwner(primaryStage);

        VBox filtersLayout = new VBox();
        filtersLayout.setSpacing(10);
        filtersLayout.setPadding(new Insets(20));
        filtersLayout.setAlignment(Pos.TOP_RIGHT);

        // Filtre par ville
        Label cityLabel = new Label("Ville:");
        ComboBox<String> cityComboBox = new ComboBox<>();
        cityComboBox.getItems().addAll("Paris", "Lyon", "Marseille", "Nice", "Bordeaux");
        filtersLayout.getChildren().addAll(cityLabel, cityComboBox);

        // Filtre par type de propriété
        Label propertyTypeLabel = new Label("Type de propriété:");
        ComboBox<String> propertyTypeComboBox = new ComboBox<>();
        propertyTypeComboBox.getItems().addAll("Appartement", "Maison", "Villa", "Studio", "Penthouse");
        filtersLayout.getChildren().addAll(propertyTypeLabel, propertyTypeComboBox);

        // Filtre par prix minimum
        Label minPriceLabel = new Label("Prix minimum:");
        TextField minPriceField = new TextField();
        filtersLayout.getChildren().addAll(minPriceLabel, minPriceField);

        // Filtre par prix maximum
        Label maxPriceLabel = new Label("Prix maximum:");
        TextField maxPriceField = new TextField();
        filtersLayout.getChildren().addAll(maxPriceLabel, maxPriceField);

        HBox buttonsContainer = new HBox();
        buttonsContainer.setSpacing(10);

        Button applyFiltersButton = new Button("Appliquer les filtres");
        applyFiltersButton.setStyle("-fx-background-color: rgb(213, 119, 195); -fx-text-fill: white; -fx-font-weight: bold;");
        applyFiltersButton.setOnAction(event -> {
            // Récupérer les valeurs des filtres et effectuer le filtrage des propriétés
            String selectedCity = cityComboBox.getValue();
            String selectedPropertyType = propertyTypeComboBox.getValue();
            String minPrice = minPriceField.getText();
            String maxPrice = maxPriceField.getText();

            // Ajoutez ici la logique pour filtrer les propriétés en fonction des valeurs sélectionnées
            System.out.println("Filtre par ville: " + selectedCity);
            System.out.println("Filtre par type de propriété: " + selectedPropertyType);
            System.out.println("Filtre par prix minimum: " + minPrice);
            System.out.println("Filtre par prix maximum: " + maxPrice);

            // Fermer la fenêtre des filtres après avoir appliqué les filtres
            filtersStage.close();
        });

        Button resetFiltersButton = new Button("Réinitialiser les filtres");
        resetFiltersButton.setOnAction(event -> {
            cityComboBox.setValue(null);
            propertyTypeComboBox.setValue(null);
            minPriceField.clear();
            maxPriceField.clear();
        });

        buttonsContainer.getChildren().addAll(applyFiltersButton, resetFiltersButton);
        filtersLayout.getChildren().add(buttonsContainer);

        Scene filtersScene = new Scene(filtersLayout);
        filtersStage.setScene(filtersScene);
        filtersStage.setTitle("Filtres des propriétés");
        filtersStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
