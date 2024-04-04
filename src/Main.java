import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        try {
            ConnectionDatabase.connect();
            // Test pour récuperer les infos des utilisateurs
            String sqlQuery = "SELECT * FROM UTILISATEUR";
            PreparedStatement statement = ConnectionDatabase.getConnection().prepareStatement(sqlQuery);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int userId = resultSet.getInt("id_user");
                String email = resultSet.getString("email");
                String name = resultSet.getString("name");

                System.out.println("ID Class.Utilisateur : " + userId);
                System.out.println("Email : " + email);
                System.out.println("Nom : " + name);
                System.out.println("----------------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionDatabase.closeConnection();
        }
    }
}
