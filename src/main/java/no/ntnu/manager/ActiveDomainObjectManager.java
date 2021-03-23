package no.ntnu.manager;

import no.ntnu.App;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class ActiveDomainObjectManager {

    private final App app;

    public ActiveDomainObjectManager(App app) {
        this.app = app;
    }

    public App getApp() {
        return app;
    }

    public Connection getConnection() throws SQLException {
        return this.app.getConnectionManager().getConnection();
    }
}
