package no.ntnu.mysql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Connection manager class. Creates connections on demand.
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
        // Ideally this should be replaced by a connection pool like HikariCP
        // (See https://github.com/brettwooldridge/HikariCP), but for the scope
        // of this assignment we did not feel that it was necessary
        return DriverManager.getConnection(this.url, this.username, this.password);
    }

    /**
     * Tests if the credentials stored in this connection manager are valid, and
     * a connection can be established by running a simple test query towards the
     * database.
     */
    public boolean testConnection() {
        System.out.println("Testing database connection...");
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();

            // This statement just tests that we are able to run a query on the database
            statement.execute("SELECT 1;");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Runs a script to create the tables if they do not exist. No error will be
     * thrown if the tables already exist. Please note that there is no verification
     * that the table structure is correct if they already exist. It is assumed that
     * if the tables exist, they were created according to the definitions of this
     * application.
     */
    public void makeTables() {
        try (Connection connection = this.getConnection()) {
            InputStream stream = this.getClass().getResourceAsStream("/init.sql");

            ScriptRunner runner = new ScriptRunner(connection, false, true);
            runner.runScript(new BufferedReader(new InputStreamReader(stream)));
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
