package no.ntnu.auth;

import no.ntnu.mysql.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthController {
    private User currentUser = null;
    private ConnectionManager connectionManager;

    public AuthController(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public void createUser(String email, String name, String password) throws SQLException {
        User user = new User(email, name, password);

        user.save(connectionManager.getConnection());

    }

    public void loginUser(String email, String password) {
        User user = this.getUserByEmail(email);
        if (user == null) {
            System.out.println("User does not exist!");
            return;
        }

        if (!password.equals(user.getPassword())) {
            System.out.println("Wrong credentials!");
            return;
        }

        currentUser = user;
    }

    private User getUserByEmail(String email) {
        User user = null;
        String queryString = "SELECT Email, Name, Password FROM User WHERE Email = ?";
        try (Connection connection = connectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setString(1, email);
            ResultSet result = preparedStatement.executeQuery();
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
