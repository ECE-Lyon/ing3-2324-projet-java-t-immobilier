package views;

import dao.DatabaseConnection;
import dao.PropertyDAO;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.stage.Modality;
import models.Utilisateur;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PropertyFilter extends Application {
    private ComboBox<String> cityComboBox;
    private ComboBox<String> typeComboBox;
    private TextField minPriceField;
    private TextField maxPriceField;
    private final PropertyDAO propertyDAO = new PropertyDAO();
    private VBox propertyContainer;

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

        Button afficherLesFiltres = new Button("Afficher les filtres");
        afficherLesFiltres.setStyle("-fx-background-color: rgb(213, 119, 195); -fx-text-fill: white; -fx-font-weight: bold;");
        addHoverAnimation(afficherLesFiltres); // Ajouter une animation au survol
        afficherLesFiltres.setOnAction(event -> showFiltersPopup(primaryStage));

        // Ajouter un bouton pour ajouter une nouvelle propriété
        Button ajouterProprieteButton = new Button("Ajouter une propriété");
        ajouterProprieteButton.setStyle("-fx-background-color: rgb(119, 195, 213); -fx-text-fill: white; -fx-font-weight: bold;");
        addHoverAnimation(ajouterProprieteButton);
        ajouterProprieteButton.setOnAction(event -> {
            // Logique pour ajouter une nouvelle propriété
            System.out.println("Ajouter une propriété");
            // Ici, vous pouvez ouvrir une nouvelle fenêtre ou un formulaire pour ajouter une propriété
        });

        // Logo de l'agence en haut de la page
        ImageView logoView = new ImageView(new Image("ece_immo.jpeg"));
        logoView.setFitWidth(200); // Ajustez la largeur du logo selon vos besoins
        logoView.setPreserveRatio(true);

        // Centrer le logo horizontalement
        HBox logoContainer = new HBox();
        logoContainer.getChildren().add(logoView);
        logoContainer.setAlignment(Pos.CENTER); // Centrer horizontalement

        GridPane filtersGrid = new GridPane();
        filtersGrid.setHgap(10);
        filtersGrid.setVgap(10);
        filtersGrid.setPadding(new Insets(10));

        // Conteneur pour les propriétés avec une barre de défilement
        ScrollPane scrollPane = new ScrollPane();

        propertyContainer = new VBox();
        propertyContainer.setAlignment(Pos.CENTER);
        propertyContainer.setSpacing(20);

        scrollPane.setContent(propertyContainer);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent;");

        // Affichage de toutes les propriétés au démarrage de l'application
        displayAllProperties();
        boolean userStatus = getCurrentUserStatut();

        // Afficher les boutons en fonction du statut de l'utilisateur
        if (userStatus) {
            root.add(ajouterProprieteButton, 1, 0);
        }

        // Ajout des éléments à la grille principale
        root.add(afficherLesFiltres, 0, 0);
        root.add(scrollPane, 0, 2, 2, 1);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Liste des propriétés immobilières");
        primaryStage.show();
    }

    public void showFiltersPopup(Stage primaryStage) {
        Stage filtersStage = new Stage();
        filtersStage.initModality(Modality.WINDOW_MODAL);
        filtersStage.initOwner(primaryStage);

        VBox filtersContainer = new VBox();
        filtersContainer.setSpacing(10);
        filtersContainer.setPadding(new Insets(20));
        filtersContainer.setAlignment(Pos.TOP_RIGHT);

        // Filtre par ville
        cityComboBox = new ComboBox<>();
        ObservableList<String> cityOptions = FXCollections.observableArrayList("Toute"); // Ajout de "Toute" comme première option
        cityOptions.addAll(propertyDAO.getDistinctValues("city", "ADDRESS"));
        cityComboBox.setItems(cityOptions);
        cityComboBox.setPromptText("Ville");

        // Filtre par type de propriété
        typeComboBox = new ComboBox<>();
        ObservableList<String> typeOptions = FXCollections.observableArrayList("Toute"); // Ajout de "Toute" comme première option
        typeOptions.addAll(propertyDAO.getDistinctValues("property_type", "PROPERTY"));
        typeComboBox.setItems(typeOptions);
        typeComboBox.setPromptText("Type de propriété");

        // Filtre par prix minimum
        minPriceField = new TextField();
        minPriceField.setPromptText("Prix minimum");

        // Filtre par prix maximum
        maxPriceField = new TextField();
        maxPriceField.setPromptText("Prix maximum");

        Button applyFiltersButton = new Button("Appliquer les filtres");
        applyFiltersButton.setStyle("-fx-background-color: rgb(213, 119, 195); -fx-text-fill: white; -fx-font-weight: bold;");
        applyFiltersButton.setOnAction(event -> {
            filterProperties(); // Appliquer les filtres
        });

        Button resetFiltersButton = new Button("Réinitialiser les filtres");
        resetFiltersButton.setOnAction(event -> resetFilters());

        filtersContainer.getChildren().addAll(new Label(""), cityComboBox, new Label(""), typeComboBox, new Label("Prix minimum:"), minPriceField, new Label("Prix maximum:"), maxPriceField, applyFiltersButton, resetFiltersButton);

        Scene filtersScene = new Scene(filtersContainer);
        filtersStage.setScene(filtersScene);
        filtersStage.setTitle("Filtres des propriétés");
        filtersStage.show();
    }

    private void displayProperties(ResultSet resultSet) throws SQLException {
        propertyContainer.getChildren().clear();
        while (resultSet.next()) {
            int id = resultSet.getInt("id_property");
            float size = resultSet.getFloat("size");
            String description = resultSet.getString("description");
            double price = resultSet.getDouble("price");
            String type = resultSet.getString("property_type");
            String city = resultSet.getString("city");
            String postalCode = resultSet.getString("postal_code");
            int numberStreet = resultSet.getInt("number_street");
            String nameStreet = resultSet.getString("name_street");

            // Création de l'image statique
            Image image = new Image(getClass().getResourceAsStream("/image1.jpg"));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(800); // Largeur de l'image fixe
            imageView.setFitHeight(600); // Hauteur de l'image fixe

            VBox addressPriceBox = new VBox();
            addressPriceBox.setAlignment(Pos.CENTER_LEFT);
            addressPriceBox.setSpacing(10);

            Label sizeLabel = new Label("Size: " + size);
            sizeLabel.setFont(Font.font("Arial", 14));

            Text descriptionLabel = new Text("Description: " + description);
            descriptionLabel.setFont(Font.font("Arial", 14));
            descriptionLabel.setFill(Color.valueOf("#777777"));
            descriptionLabel.setWrappingWidth(imageView.getFitWidth() - 20); // Largeur de la description
            descriptionLabel.setLineSpacing(2);

            addressPriceBox.getChildren().addAll(sizeLabel, descriptionLabel);

            HBox contentBox = new HBox();
            contentBox.setAlignment(Pos.CENTER_LEFT);
            contentBox.setSpacing(10);
            contentBox.getChildren().addAll(imageView, addressPriceBox);

            VBox propertyBox = new VBox();
            propertyBox.setAlignment(Pos.CENTER_LEFT);
            propertyBox.setSpacing(10);
            propertyBox.setPadding(new Insets(10));
            propertyBox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ccc; -fx-border-width: 1px;");

            Label typeLabel = new Label("Type: " + type);
            typeLabel.setFont(Font.font("Arial", 16));
            typeLabel.setTextFill(Color.valueOf("#333333"));

            Label addressLabel = new Label("Address: " + numberStreet + " " + nameStreet + ", " + postalCode + ", " + city);
            addressLabel.setFont(Font.font("Arial", 14));
            addressLabel.setTextFill(Color.valueOf("#666666"));

            Label priceLabel = new Label("Price: $" + price);
            priceLabel.setFont(Font.font("Arial", 14));
            priceLabel.setTextFill(Color.valueOf("#666666"));

            Button reserveButton = new Button("Réserver une visite");
            reserveButton.setStyle("-fx-background-color: rgb(213, 119, 195); -fx-text-fill: white; -fx-font-weight: bold;");
            addHoverAnimation(reserveButton); // Ajouter une animation au survol

            reserveButton.setOnAction(event -> {
                int propertyId = (int) reserveButton.getUserData(); // Récupérer l'ID de la propriété associé à ce bouton
                System.out.println("ID de la propriété : " + propertyId); // Afficher l'ID de la propriété dans la console
                int userId = Utilisateur.getCurrentUser().getId(); // Récupérer l'ID de l'utilisateur connecté
                System.out.println("ID de l'utilisateur : " + userId); // Afficher l'ID de la propriété dans la console
                ProgrammerVisitePage programmerVisitePage = new ProgrammerVisitePage(userId, propertyId);
                try {
                    programmerVisitePage.start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            // Lors de la création de chaque bouton "Réserver une visite", attribuez l'ID de la propriété correspondante
            reserveButton.setUserData(id); // Remplacez "id" par l'ID de la propriété associée à ce bouton

            boolean userStatus = getCurrentUserStatut();
            if(userStatus){
                // Ajouter les boutons "Modifier" et "Supprimer"
                Button editButton = new Button("Modifier");
                editButton.setStyle("-fx-background-color: rgb(119, 195, 213); -fx-text-fill: white; -fx-font-weight: bold;");
                addHoverAnimation(editButton);
                editButton.setOnAction(event -> {
                    // Logique pour modifier la propriété
                    int propertyId = (int) reserveButton.getUserData(); // Récupérer l'ID de la propriété associé à ce bouton
                    System.out.println("ID de la propriété modifie: " + propertyId); // Afficher l'ID de la propriété dans la console
                    int userId = Utilisateur.getCurrentUser().getId(); // Récupérer l'ID de l'utilisateur connecté
                    System.out.println("ID de l'utilisateur : " + userId); // Afficher l'ID de la propriété dans la console
                    ProgrammerVisitePage programmerVisitePage = new ProgrammerVisitePage(userId, propertyId);
                    try {
                        programmerVisitePage.start(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // Ici, vous pouvez ouvrir une nouvelle fenêtre ou un formulaire pour modifier la propriété
                });
                Button deleteButton = new Button("Supprimer");
                deleteButton.setStyle("-fx-background-color: rgb(213, 119, 119); -fx-text-fill: white; -fx-font-weight: bold;");
                addHoverAnimation(deleteButton);
                deleteButton.setOnAction(event -> {
                    // Logique pour supprimer la propriété
                    System.out.println("Supprimer la propriété : " + id);
                    deleteProperty(id);
                    displayAllProperties();
                });

                HBox buttonBox = new HBox(10);
                buttonBox.setAlignment(Pos.CENTER_LEFT);
                buttonBox.getChildren().addAll(editButton, deleteButton, reserveButton);

                propertyBox.getChildren().addAll(typeLabel, addressLabel, priceLabel, contentBox, buttonBox);
                propertyContainer.getChildren().add(propertyBox);
            }
            else{
                HBox buttonBox = new HBox(10);
                buttonBox.setAlignment(Pos.CENTER_LEFT);
                buttonBox.getChildren().addAll(reserveButton);

                propertyBox.getChildren().addAll(typeLabel, addressLabel, priceLabel, contentBox, buttonBox);
                propertyContainer.getChildren().add(propertyBox);
            }
        }
    }

    private void displayAllProperties() {
        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT p.id_property, p.size, p.description, p.price, p.property_type, a.city, a.postal_code, a.number_street, a.name_street " +
                    "FROM PROPERTY p " +
                    "JOIN ADDRESS a ON p.id_property = a.id_property");
            displayProperties(resultSet);
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void filterProperties() {
        String cityName = cityComboBox.getValue();
        String typeName = typeComboBox.getValue();
        String minPriceStr = minPriceField.getText();
        String maxPriceStr = maxPriceField.getText();

        String sqlQuery = "SELECT p.id_property, p.size, p.description, p.price, p.property_type, a.city, a.postal_code, a.number_street, a.name_street " +
                "FROM PROPERTY p " +
                "JOIN ADDRESS a ON p.id_property = a.id_property " +
                "WHERE ";

        if (cityName != null && !cityName.equals("Toute")) {
            sqlQuery += "a.city = '" + cityName + "'";
        } else {
            sqlQuery += "1=1"; // Condition toujours vraie pour ne pas briser la requête SQL
        }

        if (typeName != null && !typeName.equals("Toute")) {
            sqlQuery += " AND p.property_type = '" + typeName + "'";
        }

        if (!minPriceStr.isEmpty()) {
            double minPrice = Double.parseDouble(minPriceStr);
            sqlQuery += " AND p.price >= " + minPrice;
        }

        if (!maxPriceStr.isEmpty()) {
            double maxPrice = Double.parseDouble(maxPriceStr);
            sqlQuery += " AND p.price <= " + maxPrice;
        }

        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            displayProperties(resultSet);
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void resetFilters() {
        cityComboBox.setValue("Toute");
        typeComboBox.setValue("Toute");
        minPriceField.clear();
        maxPriceField.clear();
        displayAllProperties();
    }

    private void deleteProperty(int propertyId) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM PROPERTY WHERE id_property = " + propertyId);
            statement.executeUpdate("DELETE FROM ADDRESS WHERE id_property = " + propertyId);
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
