import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {
    private final String url = "jdbc:mysql://localhost:3306/aplikasitoko";
    private final String username = "root";
    private final String password = "";

    private Connection connection;

    public Koneksi() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Koneksi Berhasil");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Koneksi Gagal : " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }
}