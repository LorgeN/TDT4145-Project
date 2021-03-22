package no.ntnu.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class User {
    private String email;
    private String name;
    private String password;

    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public User getByEmail(String email) {
        return null;
    }

    public void save(Connection connection) throws SQLException {
        String queryString = "INSERT INTO User (Email, Name, Password) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(queryString)) {
            statement.setString(1, email);
            statement.setString(2, name);
            statement.setString(3, password);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public String toString() {
        return "" +
                email + ": " +
                name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
