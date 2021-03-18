package no.ntnu;

import no.ntnu.command.CommandLineRunner;
import no.ntnu.entity.tags.TagObjectManager;
import no.ntnu.mysql.ConnectionManager;
import no.ntnu.mysql.command.DatabaseConnectCommand;

/**
 * Main class for the application
 */
public class App {

    private final CommandLineRunner runner;
    private final TagObjectManager tagObjectManager;
    private ConnectionManager connectionManager;

    public App() {
        this.runner = new CommandLineRunner();

        this.tagObjectManager = new TagObjectManager(this);

        this.runner.registerCommand("dbconnect", new DatabaseConnectCommand(this));
    }

    public CommandLineRunner getRunner() {
        return runner;
    }

    public TagObjectManager getTagObjectManager() {
        return tagObjectManager;
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

        System.out.println("Connection successful! Creating tables...");
        this.connectionManager.makeTables();
        System.out.println("Tables created!");
    }
}
