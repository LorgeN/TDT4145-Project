package no.ntnu;

import no.ntnu.command.CommandLineRunner;
import no.ntnu.command.auth.*;
import no.ntnu.command.course.CreateCourseCommand;
import no.ntnu.command.course.InviteUserCommand;
import no.ntnu.command.course.SelectCourseCommand;
import no.ntnu.command.course.ViewCoursesCommand;
import no.ntnu.command.folder.CreateFolderCommand;
import no.ntnu.command.posts.*;
import no.ntnu.command.statistics.StatisticCommand;
import no.ntnu.command.tag.CreateTagCommand;
import no.ntnu.command.tag.ViewTagsCommand;
import no.ntnu.manager.*;
import no.ntnu.mysql.ConnectionManager;
import no.ntnu.mysql.command.DatabaseConnectCommand;

/**
 * Main class for the application
 */
public class App {

    private final CommandLineRunner runner;
    private final CourseObjectManager courseObjectManager;
    private final TagObjectManager tagObjectManager;
    private final PostObjectManager postObjectManager;
    private final FolderObjectManager folderObjectManager;
    private final StatisticsObjectManager statisticsObjectManager;
    private final SearchObjectManager searchObjectManager;

    private ConnectionManager connectionManager;
    private UserObjectManager userObjectManager;

    public App() {
        this.runner = new CommandLineRunner();
        this.courseObjectManager = new CourseObjectManager(this);
        this.userObjectManager = new UserObjectManager(this);
        this.statisticsObjectManager = new StatisticsObjectManager(this);
        this.searchObjectManager = new SearchObjectManager(this);

        this.tagObjectManager = new TagObjectManager(this);
        this.postObjectManager = new PostObjectManager(this);
        this.folderObjectManager = new FolderObjectManager(this);


        this.runner.registerCommand("createcourse", new CreateCourseCommand(this));
        this.runner.registerCommand("viewcourses", new ViewCoursesCommand(this));
        this.runner.registerCommand("inviteuser", new InviteUserCommand(this));
        this.runner.registerCommand("dbconnect", new DatabaseConnectCommand(this));
        this.runner.registerCommand("createfolder", new CreateFolderCommand(this));
        this.runner.registerCommand("currentuser", new CurrentUserCommand(this));
        this.runner.registerCommand("login", new LoginCommand(this));
        this.runner.registerCommand("createuser", new CreateUserCommand(this));
        this.runner.registerCommand("logout", new LogoutUserCommand(this));
        this.runner.registerCommand("printusers", new AllUsersCommand(this));
        this.runner.registerCommand("stat", new StatisticCommand(this));
        this.runner.registerCommand("selectcourse", new SelectCourseCommand(this));
        this.runner.registerCommand("search", new SearchCommand(this));
        this.runner.registerCommand("goodcomment", new GoodCommentCommand(this));
        this.runner.registerCommand("createthread", new CreateThreadCommand(this));
        this.runner.registerCommand("viewthread", new ViewThreadCommand(this));
        this.runner.registerCommand("createcomment", new CreateCommentCommand(this));
        this.runner.registerCommand("viewtags", new ViewTagsCommand(this));
        this.runner.registerCommand("createtag", new CreateTagCommand(this));
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

    public PostObjectManager getPostObjectManager() {
        return postObjectManager;
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
