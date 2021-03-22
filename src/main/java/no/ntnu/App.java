package no.ntnu;

import no.ntnu.auth.AuthController;
import no.ntnu.auth.command.*;
import no.ntnu.command.CommandLineRunner;
import no.ntnu.mysql.ConnectionManager;
import no.ntnu.mysql.command.DatabaseConnectCommand;

/**
 * Main class for the application
 */
public class App {

    private final CommandLineRunner runner;
    private ConnectionManager connectionManager;
    private AuthController authController;

    public App() {
        this.runner = new CommandLineRunner();
        this.authController = new AuthController(this.getConnectionManager());

        this.runner.registerCommand("dbconnect", new DatabaseConnectCommand(this));
        this.runner.registerCommand("login", new LoginCommand(this));
        this.runner.registerCommand("createuser", new CreateUserCommand(this));
        this.runner.registerCommand("currentuser", new CurrentUserCommand(this.authController));
        this.runner.registerCommand("logout", new LogoutUserCommand(this));
        this.runner.registerCommand("printusers", new AllUsersCommand(this));
    }

    public void startRunner() {
        this.runner.startCommandLoop();
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.authController.setConnectionManager(connectionManager);

        if (this.connectionManager == null) {
            return;
        }

        if (!this.connectionManager.testConnection()) {
            System.out.println("Connection unsuccessful!");
            return;
        }

        this.connectionManager.makeTables();
    }

    public void setAuthController(AuthController authController) {
        this.authController = authController;
    }

    public AuthController getAuthController() {
        return authController;
    }

}
