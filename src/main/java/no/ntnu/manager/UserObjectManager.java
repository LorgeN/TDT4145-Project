package no.ntnu.manager;

import no.ntnu.App;
import no.ntnu.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserObjectManager extends ActiveDomainObjectManager {

    private static final String SELECT_USER_BY_EMAIL_STATEMENT = "SELECT Email, Name, Password FROM User WHERE Email = ?";
    private static final String INSERT_USER = "INSERT INTO User (Email, Name, Password) VALUES (?, ?, ?)";
    private static final String SELECT_USERS = "SELECT Email, Name, Password FROM User";

    private User currentUser = null;

    public UserObjectManager(App app) {
        super(app);
    }

    /**
     * Creates a new user in the database
     *
     * @param email    the unique identifier for the user (email address)
     * @param name     the name of the user
     * @param password the password of the user
     * @throws SQLException if anything goes wrong when inserting user
     */
    public void createUser(String email, String name, String password) throws SQLException {
        try (Connection connection = this.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_USER)) {
            statement.setString(1, email);
            statement.setString(2, name);
            statement.setString(3, password);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Attempts to log in the user with the given credentials
     *
     * @param email    the email of the user
     * @param password the password of the user
     */
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
        List<User> users = new ArrayList<>();
        try (Connection connection = this.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_USERS)) {
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
     * Returns the user with the given email address
     *
     * @param email email address
     * @return the user matching the email
     */
    public User getUserByEmail(String email) {
        User user = null;

        try (Connection connection = this.getConnection()) {
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

    public boolean isAuthenticated() {
        return this.currentUser != null;
    }
}
