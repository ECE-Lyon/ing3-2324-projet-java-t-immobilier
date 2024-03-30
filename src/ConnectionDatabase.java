import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDatabase {
    private static Connection connection;

    public static void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:8889/immobilier_ing3", "root", "root");
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
}
