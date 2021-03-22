package no.ntnu;

import no.ntnu.auth.UserObjectManager;
import no.ntnu.auth.command.*;
import no.ntnu.command.CommandLineRunner;
import no.ntnu.course.CourseObjectManager;
import no.ntnu.course.command.SelectCourseCommand;
import no.ntnu.folder.FolderObjectManager;
import no.ntnu.folder.command.CreateFolderCommand;
import no.ntnu.mysql.ConnectionManager;
import no.ntnu.mysql.command.DatabaseConnectCommand;
import no.ntnu.search.SearchObjectManager;
import no.ntnu.search.command.SearchCommand;
import no.ntnu.statistics.StatisticsObjectManager;
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
    private UserObjectManager userObjectManager;
    private FolderObjectManager folderObjectManager;
    private StatisticsObjectManager statisticsObjectManager;
    private SearchObjectManager searchObjectManager;

    public App() {
        this.runner = new CommandLineRunner();
        this.courseObjectManager = new CourseObjectManager(this);
        this.userObjectManager = new UserObjectManager(this);
        this.statisticsObjectManager = new StatisticsObjectManager(this);
        this.searchObjectManager = new SearchObjectManager(this);

        this.tagObjectManager = new TagObjectManager(this);
        this.folderObjectManager = new FolderObjectManager(this);

        this.runner.registerCommand("dbconnect", new DatabaseConnectCommand(this));
        this.runner.registerCommand("login", new LoginCommand(this));
        this.runner.registerCommand("createfolder", new CreateFolderCommand(this));
        this.runner.registerCommand("currentuser", new CurrentUserCommand(this));
        this.runner.registerCommand("login", new LoginCommand(this));
        this.runner.registerCommand("createuser", new CreateUserCommand(this));
        this.runner.registerCommand("logout", new LogoutUserCommand(this));
        this.runner.registerCommand("printusers", new AllUsersCommand(this));
        this.runner.registerCommand("stat", new StatisticCommand(this));
        this.runner.registerCommand("selectcourse", new SelectCourseCommand(this));
        this.runner.registerCommand("search", new SearchCommand(this));
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

    public StatisticsObjectManager getStatisticsController() {
        return statisticsObjectManager;
    }

    public SearchObjectManager getSearchController() {
        return searchObjectManager;
    }

    public FolderObjectManager getFolderController() {
        return folderObjectManager;
    }

    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.userObjectManager.setConnectionManager(connectionManager);
        this.statisticsObjectManager.setConnectionManager(connectionManager);
        this.searchObjectManager.setConnectionManager(connectionManager);
        this.folderObjectManager.setConnectionManager(connectionManager);

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

    public void setAuthController(UserObjectManager userObjectManager) {
        this.userObjectManager = userObjectManager;
    }

    public UserObjectManager getAuthController() {
        return userObjectManager;
    }

}
