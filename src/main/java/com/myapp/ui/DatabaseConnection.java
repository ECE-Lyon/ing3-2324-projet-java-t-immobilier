package com.myapp.ui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Chemin vers votre base de données
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/immobilier_ing3";

    // Méthode pour établir une connexion à la base de données
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL, "root","root");
    }
}
