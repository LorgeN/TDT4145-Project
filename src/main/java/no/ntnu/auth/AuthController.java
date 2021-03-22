package no.ntnu.auth;

import no.ntnu.mysql.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthController {

    private static final String SELECT_USER_BY_EMAIL_STATEMENT = "SELECT Email, Name, Password FROM User WHERE Email = ?";

    private User currentUser = null;
    private ConnectionManager connectionManager;

    public AuthController(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public void createUser(String email, String name, String password) throws SQLException {
        User user = new User(email, name, password);
        user.save(this.connectionManager.getConnection());
    }

    public boolean loginUser(String email, String password) {
        User user = this.getUserByEmail(email);
        if (user == null) {
            System.out.println("User does not exist!");
            return false;
        }

        if (!password.equals(user.getPassword())) {
            System.out.println("Wrong credentials!");
            return false;
        }

        currentUser = user;
        return true;
    }

    public User getUserByEmail(String email) {
        User user = null;

        try (Connection connection = this.connectionManager.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_USER_BY_EMAIL_STATEMENT);
            statement.setString(1, email);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                user = new User(result.getString("Email"), result.getString("Name"), result.getString("Password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public boolean isAuthenticated() {
        return this.currentUser != null;
    }
}
