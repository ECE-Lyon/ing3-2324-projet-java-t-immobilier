import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionDatabase {
    private static Connection connection;

    public static void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/immobilier_ing3", "root", "root");
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Méthode pour vérifier les informations de connexion de l'utilisateur
    public static boolean verifyConnection(String email, String password) {
        String query = "SELECT * FROM UTILISATEUR WHERE email = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Renvoie vrai si un utilisateur correspond aux informations fournies
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
