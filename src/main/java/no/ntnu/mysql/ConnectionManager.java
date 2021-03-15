package no.ntnu.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Connection manager class. Creates connections on demand.
 * <p>
 * If you intend to actually use this for something, use a connection
 * pool like HikariCP.
 */
public class ConnectionManager {

    private final String url;
    private final String username;
    private final String password;

    /**
     * Creates a new connection manager instance. Does not maintain any connections.
     *
     * @param url      The full database connection url, e. g. jdbc:mysql://localhost:3306/database
     * @param username The database server username
     * @param password The database server password
     */
    public ConnectionManager(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * Creates a new connection to the database server
     *
     * @return The newly created {@link Connection connection}
     * @throws SQLException If an error occurs
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.url, this.username, this.password);
    }

    /**
     * Tests if the credentials stored in this connection manager are valid, and
     * a connection can be established by running a simple test query towards the
     * database.
     */
    public void testConnection() {
        System.out.println("Testing database connection...");
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();

            statement.execute("SELECT 1");

            System.out.println("Connection test successful!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
