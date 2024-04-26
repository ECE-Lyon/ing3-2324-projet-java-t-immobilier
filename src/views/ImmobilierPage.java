package views;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import models.Property;
import dao.PropertyDAO;
import java.util.List;

public class ImmobilierPage extends Application {
    private final PropertyDAO propertyDAO = new PropertyDAO();
    private GridPane annoncesGrid;

    private static final double CARD_WIDTH = 400.0;
    private final String city;
    private final String propertyType;
    private final double price;
    private final double size;
    private final boolean hasPool;
    private final boolean hasGarden;


    public ImmobilierPage(String city, String propertyType, double price, double size, boolean hasPool, boolean hasGarden) {
        this.city = city;
        this.propertyType = propertyType;
        this.price = price;
        this.size = size;
        this.hasPool = hasPool;
        this.hasGarden = hasGarden;

    }

    @Override
    public void start(Stage primaryStage) {
        annoncesGrid = new GridPane();
        annoncesGrid.setHgap(20);
        annoncesGrid.setVgap(20);
        annoncesGrid.setAlignment(Pos.CENTER);
        annoncesGrid.setPadding(new Insets(25, 25, 25, 25));

        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/ece_immo.jpeg")));
        logo.setFitWidth(120);
        logo.setPreserveRatio(true);

        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color: white; -fx-font-size: 18px; -fx-padding: 15px 0; -fx-border-color: #f6f6f6; -fx-border-width: 0 0 1 0;");

        Menu menuImmobilier = new Menu("Immobilier");
        menuImmobilier.getItems().addAll(new MenuItem("Acheter"), new MenuItem("Vendre"), new MenuItem("Louer"));
        Menu menuArtDeVivre = new Menu("Art de Vivre");
        menuArtDeVivre.getItems().addAll(new MenuItem("Culture"), new MenuItem("Gastronomie"), new MenuItem("Voyages"));
        Menu menuServices = new Menu("Services");
        menuServices.getItems().addAll(new MenuItem("Conseils"), new MenuItem("Estimations"), new MenuItem("Financements"));
        menuBar.getMenus().addAll(menuImmobilier, menuArtDeVivre, menuServices);

        ImageView profileIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon.jpg")));
        profileIcon.setFitWidth(30);
        profileIcon.setFitHeight(30);
        Button profileButton = new Button("", profileIcon);
        profileButton.setStyle("-fx-background-color: transparent;");

        HBox logoMenuBarBox = new HBox(10, logo, menuBar, profileButton);
        logoMenuBarBox.setStyle("-fx-background-color: WHITE;");
        logoMenuBarBox.setPadding(new Insets(15));
        logoMenuBarBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(menuBar, Priority.ALWAYS);

        ComboBox<String> comboBoxCity = new ComboBox<>();
        comboBoxCity.setItems(propertyDAO.getDistinctValues("city", "ADDRESS"));
        comboBoxCity.setPromptText("Ville");

        ComboBox<String> comboBoxBuyRent = new ComboBox<>();
        comboBoxBuyRent.setItems(propertyDAO.getDistinctValues("property_type", "PROPERTY"));
        comboBoxBuyRent.setPromptText("Type de propriété");

        TextField budgetTextField = new TextField();
        budgetTextField.setPromptText("Prix maximum");

        TextField bedroomsTextField = new TextField();
        bedroomsTextField.setPromptText("Pièces");

        TextField areaTextField = new TextField();
        areaTextField.setPromptText("Surface");

        Button advancedSearchButton = new Button("Recherche avancée");
        advancedSearchButton.setStyle("-fx-background-color: rgb(213, 119, 195); -fx-text-fill: white;");
        advancedSearchButton.setOnAction(event -> {
            try {
                double maxPrice = budgetTextField.getText().isEmpty() ? Double.MAX_VALUE : Double.parseDouble(budgetTextField.getText());
                double maxSize = areaTextField.getText().isEmpty() ? Double.MAX_VALUE : Double.parseDouble(areaTextField.getText());
                List<Property> filteredProperties = propertyDAO.searchProperties(comboBoxCity.getValue(), comboBoxBuyRent.getValue(), maxPrice, maxSize, hasPool, hasGarden);
                annoncesGrid.getChildren().clear();
                populateGrid(annoncesGrid, filteredProperties);
            } catch (NumberFormatException e) {
                showAlert("Input Error", "Please enter valid numerical values.");
            }
        });

        HBox filtersBox = new HBox(10, comboBoxCity, comboBoxBuyRent, budgetTextField, bedroomsTextField, areaTextField, advancedSearchButton);
        filtersBox.setAlignment(Pos.CENTER);
        filtersBox.setSpacing(5);

        VBox topContent = new VBox(logoMenuBarBox, filtersBox);
        topContent.setAlignment(Pos.CENTER);
        topContent.setSpacing(10);
        topContent.setPadding(new Insets(10));

        GridPane annoncesGrid = new GridPane();
        annoncesGrid.setHgap(20);
        annoncesGrid.setVgap(20);
        annoncesGrid.setAlignment(Pos.CENTER);
        annoncesGrid.setPadding(new Insets(25, 25, 25, 25));
        populateGrid(annoncesGrid, propertyDAO.searchProperties(city, propertyType, price, size, hasPool, hasGarden));

        ScrollPane scrollPane = new ScrollPane(annoncesGrid);
        scrollPane.setFitToWidth(true);

        BorderPane root = new BorderPane();
        root.setTop(topContent);
        root.setCenter(scrollPane);

        Scene scene = new Scene(root, 1024, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Liste des propriétés immobilières");
        primaryStage.show();
    }

    private void populateGrid(GridPane grid, List<Property> properties) {
        int numCols = 3;
        int row = 0;
        for (int i = 0; i < properties.size(); i++) {
            Property property = properties.get(i);
            VBox annonce = createAnnonce(property);
            grid.add(annonce, i % numCols, row);
            if (i % numCols == 2) row++;
        }
    }

    private VBox createAnnonce(Property property) {
        VBox annonceBox = new VBox(10);
        annonceBox.setPadding(new Insets(15));
        annonceBox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #c0c0c0;");
        annonceBox.setAlignment(Pos.CENTER_LEFT);

        Image image = new Image(getClass().getResourceAsStream(property.getImagePath()));
        double imageWidth = CARD_WIDTH - 30;
        double imageHeight = imageWidth * (image.getHeight() / image.getWidth());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(imageWidth);
        imageView.setFitHeight(imageHeight);

        Label titleLabel = new Label(property.getTitle());
        titleLabel.setFont(Font.font("Arial", 20));

        Label detailsLabel = new Label(property.getDescription() + " | Size: " + property.getSize() + " | Price: " + property.getPrice() + " | Rooms: " + property.getNumberOfRooms());
        detailsLabel.setFont(Font.font("Arial", 16));

        Button visitButton = new Button("Programmer une visite");
        visitButton.setStyle("-fx-background-color: rgb(213, 119, 195); -fx-text-fill: white;");

        annonceBox.getChildren().addAll(imageView, titleLabel, detailsLabel, visitButton);

        return annonceBox;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}