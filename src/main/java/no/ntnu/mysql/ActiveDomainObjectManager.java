package no.ntnu.mysql;

import no.ntnu.App;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class ActiveDomainObjectManager {

    private final App app;
    protected ConnectionManager connectionManager;

    public ActiveDomainObjectManager(App app) {
        this.app = app;
    }

    public App getApp() {
        return app;
    }

    public Connection getConnection() throws SQLException {
        return this.app.getConnectionManager().getConnection();
    }

    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
}
