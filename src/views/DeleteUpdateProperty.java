package views;

import dao.DeleteUpdatePropertyDAO;
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
import models.Address;
import models.Property;

import java.util.List;

public class DeleteUpdateProperty extends Application {

    private List<Property> properties;
    private List<Address> addresses;
    private ComboBox<Property> propertyComboBox;
    private ComboBox<Address> addressComboBox;
    private TextField streetNumberField;
    private TextField streetNameField;
    private TextField postalCodeField;
    private TextField cityField;
    private TextField priceField;
    private TextField sizeField;
    private TextField roomsField;
    private CheckBox gardenCheckBox;
    private CheckBox poolCheckBox;
    private RadioButton houseRadio;
    private RadioButton apartmentRadio;

    private DeleteUpdatePropertyDAO deleteUpdatePropertyDAO = new DeleteUpdatePropertyDAO(); // Instance du DAO

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Modifier une Propriété");

        // Initialisation des propriétés depuis la base de données
        initProperties();

        // Setup de la GUI
        GridPane root = configureRootLayout();
        VBox propertyLayout = configurePropertyLayout();
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane configureRootLayout() {
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(20);
        root.setVgap(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #FFFFFF;");
        ImageView logoView = new ImageView(new Image("ece_immo.jpeg"));
        logoView.setFitWidth(200);
        logoView.setPreserveRatio(true);
        HBox logoContainer = new HBox(logoView);
        logoContainer.setAlignment(Pos.CENTER);
        root.add(logoContainer, 0, 0, 2, 1);
        return root;
    }

    private VBox configurePropertyLayout() {
        VBox propertyLayout = new VBox(20);
        propertyLayout.setAlignment(Pos.CENTER_LEFT);
        propertyLayout.getChildren().addAll(
                createLabeledField("Sélectionnez une Propriété:", propertyComboBox),
                createLabeledField("Numéro de Rue:", streetNumberField),
                createLabeledField("Nom de Rue:", streetNameField),
                createLabeledField("Code Postal:", postalCodeField),
                createLabeledField("Ville:", cityField),
                createLabeledField("Prix:", priceField),
                createLabeledField("Superficie (m²):", sizeField),
                createLabeledField("Nombre de Chambres:", roomsField),
                createLabeledField("Jardin:", gardenCheckBox),
                createLabeledField("Piscine:", poolCheckBox),
                createLabeledField("Type:", new HBox(10, houseRadio, apartmentRadio)),
                new HBox(10, configureModifyButton(), configureDeleteButton())
        );
        return propertyLayout;
    }

    private HBox createLabeledField(String labelText, Node field) {
        Label label = new Label(labelText);
        label.setFont(Font.font(14));
        HBox container = new HBox(10);
        container.setAlignment(Pos.CENTER_LEFT);
        container.getChildren().addAll(label, field);
        return container;
    }



    private void initProperties() {
        properties = DeleteUpdatePropertyDAO.getAllProperties(); // Récupération des propriétés
        propertyComboBox = new ComboBox<>();
        propertyComboBox.getItems().addAll(properties);
        propertyComboBox.setPromptText("Sélectionnez une propriété");
        propertyComboBox.setOnAction(e -> loadPropertyData(propertyComboBox.getValue(), addressComboBox.getValue()));
    }

    private void loadPropertyData(Property property, Address address) {
        if (property != null) {
            streetNumberField.setText(String.valueOf(address.getNumberStreet()));
            streetNameField.setText(address.getNameStreet());
            postalCodeField.setText(String.valueOf(address.getPostalCode()));
            cityField.setText(address.getCity());
            priceField.setText(String.valueOf(property.getPrice()));
            sizeField.setText(String.valueOf(property.getSize()));
            roomsField.setText(String.valueOf(property.getNbRoom()));
            gardenCheckBox.setSelected(property.isHasGarden());
            poolCheckBox.setSelected(property.isHasPool());
            if ("Maison".equals(property.getPropertyType())) {
                houseRadio.setSelected(true);
            } else {
                apartmentRadio.setSelected(true);
            }
        }
    }

    private Button configureModifyButton() {
        Button modifyButton = new Button("Modifier");
        modifyButton.setStyle("-fx-background-color: rgb(213, 119, 195); -fx-text-fill: white;");
        modifyButton.setFont(Font.font(16));
        modifyButton.setOnAction(e -> updateProperty(propertyComboBox.getValue(), addressComboBox.getValue()));
        return modifyButton;
    }

    private Button configureDeleteButton() {
        Button deleteButton = new Button("Supprimer");
        deleteButton.setStyle("-fx-background-color: rgb(213, 119, 195); -fx-text-fill: white;");
        deleteButton.setFont(Font.font(16));
        deleteButton.setOnAction(e -> deleteProperty(propertyComboBox.getValue()));
        return deleteButton;
    }

    private void updateProperty(Property property, Address address) {
        DeleteUpdatePropertyDAO.updateProperty(property, address);
    }

    private void deleteProperty(Property property) {
        DeleteUpdatePropertyDAO.deleteProperty(property.getIdProperty());
        properties.remove(property);
        propertyComboBox.getItems().remove(property);
    }

    public static void main(String[] args) {
        launch(args);
    }
}