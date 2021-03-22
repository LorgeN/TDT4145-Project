package no.ntnu.auth;

import no.ntnu.mysql.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AuthController {
    private User currentUser = null;
    private ConnectionManager connectionManager;

    public AuthController(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * Creates a new user in the database
     *
     * @param email    the unique identifier for the user (email address)
     * @param name
     * @param password
     * @throws SQLException if anything goes wrong when inserting user
     */
    public void createUser(String email, String name, String password) throws SQLException {
        User user = new User(email, name, password);

        user.save(connectionManager.getConnection());

    }

    /**
     * Attempts to log in the user with the given credentials
     *
     * @param email
     * @param password
     */
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

    /**
     * Logs out the current user if any
     */
    public void logoutUser() {
        if (this.currentUser != null) {
            this.currentUser = null;
            System.out.println("You are now logged out!");
        } else {
            System.out.println("No user is logged in yet");
        }

    }

    /**
     * Gets a list of all users in the database
     *
     * @return the list of all users
     */
    public Collection<User> getAllUsers() {
        String queryString = "SELECT Email, Name, Password FROM User";
        List<User> users = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection(); PreparedStatement statement = connection.prepareStatement(queryString)) {
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                users.add(new User(result.getString("Email"), result.getString("Name"), result.getString("Password")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return users;
    }

    /**
     * Returns the user with the given email adress
     *
     * @param email email address
     * @return the user matching the email
     */
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
