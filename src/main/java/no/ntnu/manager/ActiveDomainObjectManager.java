package no.ntnu.manager;

import no.ntnu.App;
import no.ntnu.mysql.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * A manager class for an active domain object. Contains logic for
 * interacting with the tables relevant to the entity class this manager
 * handles, such as inserting, updating and selecting.
 */
public abstract class ActiveDomainObjectManager {

    private final App app;

    /**
     * Creates a new instance of this manager for the given {@link App app}.
     *
     * @param app The app instance
     */
    public ActiveDomainObjectManager(App app) {
        this.app = app;
    }

    /**
     * @return The {@link App app} instance
     */
    public App getApp() {
        return app;
    }

    /**
     * Gets a connection to the database from the {@link ConnectionManager}.
     *
     * @return A connection to the database
     * @throws SQLException If an error occurs
     * @see ConnectionManager#getConnection()
     */
    public Connection getConnection() throws SQLException {
        return this.app.getConnectionManager().getConnection();
    }
}
