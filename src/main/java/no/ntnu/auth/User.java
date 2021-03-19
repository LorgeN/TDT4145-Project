package no.ntnu.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;
        return Objects.equals(getEmail(), user.getEmail())
                && Objects.equals(getName(), user.getName())
                && Objects.equals(getPassword(), user.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail(), getName(), getPassword());
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
