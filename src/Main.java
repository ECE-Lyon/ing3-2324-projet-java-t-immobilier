import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:8889/immobilier_ing3", "root", "root");

            String sqlQuery = "SELECT * FROM UTILISATEUR";
            statement = connection.prepareStatement(sqlQuery);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int userId = resultSet.getInt("id_user");
                String email = resultSet.getString("email");
                String name = resultSet.getString("name");

                System.out.println("ID Utilisateur : " + userId);
                System.out.println("Email : " + email);
                System.out.println("Nom : " + name);
                System.out.println("----------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
