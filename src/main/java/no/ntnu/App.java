package no.ntnu;

import no.ntnu.auth.AuthController;
import no.ntnu.auth.command.CreateUserCommand;
import no.ntnu.auth.command.CurrentUserCommand;
import no.ntnu.auth.command.LoginCommand;
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
        this.runner.registerCommand("login", new LoginCommand(this.authController));
        this.runner.registerCommand("createuser", new CreateUserCommand(this.authController));
        this.runner.registerCommand("currentuser", new CurrentUserCommand(this.authController));
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
    }

    public void setAuthController(AuthController authController) {
        this.authController = authController;
    }

    public AuthController getAuthController() {
        return authController;
    }
}
