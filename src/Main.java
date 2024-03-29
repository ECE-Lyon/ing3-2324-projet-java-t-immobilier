import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;
import java.sql.SQLException;


public class Main {
    public static void main(String[] args) throws SQLException{

        try (Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:8888/immobilier_ing3","root","root")){
            try(PreparedStatement statement= connection.prepareStatement("SELECT * FROM ")){
                statement.setString(1,);

            }
        }

    }
}
