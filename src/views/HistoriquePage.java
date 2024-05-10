package views;

import dao.DatabaseConnection;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Transaction;
import models.Utilisateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class HistoriquePage extends Application {

    private final Utilisateur client;

    public HistoriquePage(Utilisateur client) {
        this.client = client;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Historique des transactions pour " + client.getName());

        // Création de la table des transactions
        TableView<Transaction> transactionTable = new TableView<>();
        TableColumn<Transaction, Integer> idPropertyColumn = new TableColumn<>("ID Propriété");
        idPropertyColumn.setCellValueFactory(cellData -> cellData.getValue().idPropertyProperty().asObject());
        TableColumn<Transaction, String> propertyDescriptionColumn = new TableColumn<>("Description Propriété");
        propertyDescriptionColumn.setCellValueFactory(cellData -> cellData.getValue().propertyDescriptionProperty());
        TableColumn<Transaction, Double> propertyPriceColumn = new TableColumn<>("Prix Propriété");
        propertyPriceColumn.setCellValueFactory(cellData -> cellData.getValue().propertyPriceProperty().asObject());
        TableColumn<Transaction, Date> transactionDateColumn = new TableColumn<>("Date de Transaction");
        transactionDateColumn.setCellValueFactory(cellData -> cellData.getValue().transactionDateProperty());

        transactionTable.getColumns().addAll(idPropertyColumn, propertyDescriptionColumn, propertyPriceColumn, transactionDateColumn);
        transactionTable.setItems(getTransactionsForClient(client));

        // Mise en page
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().add(transactionTable);

        // Affichage de la scène
        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private ObservableList<Transaction> getTransactionsForClient(Utilisateur client) {
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT p.id_property, p.description, p.price, v.date_visit " +
                            "FROM PROPERTY p " +
                            "JOIN VISIT v ON p.id_property = v.id_property " +
                            "LEFT JOIN CLIENT c_buyer ON p.id_client = c_buyer.id_client " +
                            "LEFT JOIN CLIENT c_seller ON p.id_employee = c_seller.id_client " +
                            "WHERE c_buyer.id_user = ? OR c_seller.id_user = ?"
            );

            statement.setInt(1, client.getId());
            statement.setInt(2, client.getId());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int idProperty = resultSet.getInt("id_property");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                Date date = resultSet.getDate("date_visit");
                transactions.add(new Transaction(idProperty, description, price, date));
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
