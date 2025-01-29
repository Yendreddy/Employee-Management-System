package EmpManagementSystem;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleConnection {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:oracle:thin:@//localhost:1521/xepdb1";
        String username = "Yendreddy";
        String password = "Durga1710";

        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            System.out.println("Connected to Oracle database!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
