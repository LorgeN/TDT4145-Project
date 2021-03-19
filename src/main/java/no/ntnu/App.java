package no.ntnu;

import no.ntnu.auth.AuthController;
import no.ntnu.auth.command.CreateUserCommand;
import no.ntnu.auth.command.CurrentUserCommand;
import no.ntnu.auth.command.LoginCommand;
import no.ntnu.command.CommandLineRunner;
import no.ntnu.course.CourseObjectManager;
import no.ntnu.folder.FolderController;
import no.ntnu.folder.command.CreateFolderCommand;
import no.ntnu.mysql.ConnectionManager;
import no.ntnu.mysql.command.DatabaseConnectCommand;
import no.ntnu.statistics.StatisticsController;
import no.ntnu.statistics.command.StatisticCommand;
import no.ntnu.tags.TagObjectManager;

/**
 * Main class for the application
 */
public class App {

    private final CommandLineRunner runner;
    private final CourseObjectManager courseObjectManager;
    private final TagObjectManager tagObjectManager;

    private ConnectionManager connectionManager;
    private AuthController authController;
    private FolderController folderController;
    private StatisticsController statisticsController;

    public App() {
        this.runner = new CommandLineRunner();
        this.courseObjectManager = new CourseObjectManager(this);
        this.authController = new AuthController(this.getConnectionManager());
        this.statisticsController = new StatisticsController();

        this.tagObjectManager = new TagObjectManager(this);
        this.folderController = new FolderController(this.getConnectionManager());

        this.runner.registerCommand("dbconnect", new DatabaseConnectCommand(this));
        this.runner.registerCommand("login", new LoginCommand(this.authController));
        this.runner.registerCommand("createuser", new CreateUserCommand(this.authController));
        this.runner.registerCommand("createfolder", new CreateFolderCommand(this));
        this.runner.registerCommand("currentuser", new CurrentUserCommand(this));
        this.runner.registerCommand("stat", new StatisticCommand(this));
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

    public CourseObjectManager getCourseObjectManager() {
        return courseObjectManager;
    }

    public StatisticsController getStatisticsController() {
        return statisticsController;
    }

    public FolderController getFolderController() {
        return folderController;
    }

    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.authController.setConnectionManager(connectionManager);
        this.statisticsController.setConnectionManager(connectionManager);
        this.folderController.setConnectionManager(connectionManager);

        if (this.connectionManager == null) {
            return;
        }

        if (!this.connectionManager.testConnection()) {
            System.out.println("Connection unsuccessful!");
            return;
        }

        System.out.println("Connection to database successful! Ensuring tables are present...");
        this.connectionManager.makeTables();
        System.out.println("Finished checking tables!");
    }

    public void setAuthController(AuthController authController) {
        this.authController = authController;
    }

    public AuthController getAuthController() {
        return authController;
    }

}
