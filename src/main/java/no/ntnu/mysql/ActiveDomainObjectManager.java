package no.ntnu.mysql;

import no.ntnu.App;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class ActiveDomainObjectManager {

    private final App app;

    public ActiveDomainObjectManager(App app) {
        this.app = app;

        String tableStatement = this.getCreateTableStatement();
        if (tableStatement == null) {
            return;
        }

        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(tableStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return this.app.getConnectionManager().getConnection();
    }

    public abstract String getCreateTableStatement();
}
