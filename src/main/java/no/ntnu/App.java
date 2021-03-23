package no.ntnu;

import no.ntnu.command.CommandLineRunner;
import no.ntnu.command.auth.*;
import no.ntnu.command.course.CreateCourseCommand;
import no.ntnu.command.course.InviteUserCommand;
import no.ntnu.command.course.SelectCourseCommand;
import no.ntnu.command.course.ViewCoursesCommand;
import no.ntnu.command.database.DatabaseConnectCommand;
import no.ntnu.command.folder.CreateFolderCommand;
import no.ntnu.command.posts.*;
import no.ntnu.command.statistics.StatisticCommand;
import no.ntnu.command.tag.CreateTagCommand;
import no.ntnu.command.tag.ViewTagsCommand;
import no.ntnu.manager.*;
import no.ntnu.mysql.ConnectionManager;

/**
 * Main class for the application, containing instances of the various
 * {@link ActiveDomainObjectManager managers}.
 */
public class App {

    private final CommandLineRunner runner;

    // Manager instances
    private final CourseObjectManager courseObjectManager;
    private final TagObjectManager tagObjectManager;
    private final PostObjectManager postObjectManager;
    private final FolderObjectManager folderObjectManager;
    private final StatisticsObjectManager statisticsObjectManager;
    private final UserObjectManager userObjectManager;

    private ConnectionManager connectionManager;

    /**
     * Creates a new instance of the application, and initializes all the managers
     * etc. Before the application can be used, a connection manager must be configured.
     */
    public App() {
        this.runner = new CommandLineRunner();
        this.courseObjectManager = new CourseObjectManager(this);
        this.userObjectManager = new UserObjectManager(this);
        this.statisticsObjectManager = new StatisticsObjectManager(this);

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

    /**
     * @return The {@link CommandLineRunner command runner} instance
     */
    public CommandLineRunner getRunner() {
        return runner;
    }

    /**
     * Starts the infinite command loop. This is a blocking operation,
     * and the thread this is called on will not be released until the
     * system is exited.
     */
    public void startRunner() {
        this.runner.startCommandLoop();
    }

    /**
     * @return The {@link ConnectionManager SQL connection manager} instance
     */
    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    /**
     * Updates the connection manager to be used in this application instance.
     * Will test the connection using a simple test statement, and if the test
     * is successful a script to create the required tables will be executed.
     *
     * @param connectionManager The new {@link ConnectionManager connection manager}
     * @see ConnectionManager#testConnection()
     * @see ConnectionManager#makeTables()
     */
    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;

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

    /**
     * @return The {@link TagObjectManager} instance
     */
    public TagObjectManager getTagObjectManager() {
        return tagObjectManager;
    }

    /**
     * @return The {@link CourseObjectManager} instance
     */
    public CourseObjectManager getCourseObjectManager() {
        return courseObjectManager;
    }

    /**
     * @return The {@link StatisticsObjectManager} instance
     */
    public StatisticsObjectManager getStatisticsManager() {
        return statisticsObjectManager;
    }

    /**
     * @return The {@link FolderObjectManager} instance
     */
    public FolderObjectManager getFolderManager() {
        return folderObjectManager;
    }

    /**
     * @return The {@link PostObjectManager} instance
     */
    public PostObjectManager getPostObjectManager() {
        return postObjectManager;
    }

    /**
     * @return The {@link UserObjectManager} instance
     */
    public UserObjectManager getUserManager() {
        return userObjectManager;
    }
}
