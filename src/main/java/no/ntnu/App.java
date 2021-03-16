package no.ntnu;

import no.ntnu.command.CommandLineRunner;
import no.ntnu.mysql.ConnectionManager;
import no.ntnu.mysql.command.DatabaseConnectCommand;

/**
 * Main class for the application
 */
public class App {

    private final CommandLineRunner runner;
    private ConnectionManager connectionManager;

    public App() {
        this.runner = new CommandLineRunner();

        this.runner.registerCommand("dbconnect", new DatabaseConnectCommand(this));
    }

    public void startRunner() {
        this.runner.startCommandLoop();
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;

        if (this.connectionManager == null) {
            return;
        }

        if (!this.connectionManager.testConnection()) {
            System.out.println("Connection unsuccessful!");
            return;
        }

        this.connectionManager.makeTables();
    }
}
