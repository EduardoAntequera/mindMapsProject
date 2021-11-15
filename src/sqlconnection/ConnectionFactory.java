package sqlconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public static final String URL = "jdbc:sqlite:MindMapsProject.sqlite3";
    public static final String USER = "testuser";
    public static final String PASS = "testpass";

    /**
     * Get a connection to database
     * @return Connection object
     */
    public static Connection getConnection()
    {
        try {
            System.out.println("Connection to SQLite has been established.");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException ex) {
            System.out.println("Error connecting to the database");
            throw new RuntimeException("Error connecting to the database", ex);
        }
    }
}
