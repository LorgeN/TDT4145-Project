package no.ntnu.posts;

import no.ntnu.App;
import no.ntnu.auth.User;
import no.ntnu.mysql.ActiveDomainObjectManager;
import no.ntnu.posts.command.CreateCommentCommand;
import no.ntnu.posts.command.CreateThreadCommand;
import no.ntnu.posts.command.ViewThreadCommand;

import java.awt.Color;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostObjectManager extends ActiveDomainObjectManager {

    private static final String INSERT_THREAD_STATEMENT = "INSERT INTO thread(Title, CourseId, FolderId, Tag) " +
            "VALUES (?, ?, ?, ?);";
    private static final String INSERT_POST_STATEMENT = "INSERT INTO post(ThreadId, IsRoot, Anonymous, PostedAt, " +
            "Text, CreatedByUser) VALUES (?, ?, ?, ?, ?, ?);";

    // Statement will enforce anonymous choice for the course
    private static final String SELECT_POSTS_STATEMENT = "SELECT P.PostId, P.ThreadId, " +
            "P.IsRoot, P.PostedAt, P.Text, P.CreatedByUser, " +
            "IF(P.Anonymous AND EXISTS(SELECT * " +
            "FROM thread T " +
            "INNER JOIN course C on T.CourseId = C.CourseId " +
            "WHERE AllowAnonymous = TRUE AND T.ThreadId = P.ThreadId), " +
            "TRUE, FALSE) AS Anonymous " +
            "FROM Post P " +
            "WHERE P.ThreadId=?;";

    private static final String SELECT_THREAD_STATEMENT = "SELECT *, " + Thread.NOT_ANSWERED + " AS Answered " +
        "FROM Thread T WHERE ThreadId = ?;";

    private static final String SELECT_THREAD_REPLY_STATUS_STATEMENT = "SELECT *, " +
            "CASE " +
            "WHEN EXISTS(SELECT * FROM (Post P JOIN User U ON P.CreatedByUser = U.Email) JOIN Participant PP ON U.Email = PP.User WHERE PP.IsInstructor = TRUE AND P.ThreadId = P.ThreadId AND PP.CourseId = ?) THEN " + Thread.INSTRUCTOR_ANSWERED + " " +
            "WHEN (SELECT COUNT(*) FROM Post P3 WHERE P3.ThreadId = T.ThreadId) > 1 THEN " + Thread.ANSWERED + " " +
            "ELSE " + Thread.NOT_ANSWERED + " " +
            "END AS Answered " +
        "FROM Thread T WHERE T.ThreadId = ?;";

    public PostObjectManager(App app) {
        super(app);

        app.getRunner().registerCommand("createthread", new CreateThreadCommand(app));
        app.getRunner().registerCommand("viewthread", new ViewThreadCommand(app));
        app.getRunner().registerCommand("createcomment", new CreateCommentCommand(app));
    }

    public Thread getThread(int threadId, Integer courseId) {
        String query = courseId == null? SELECT_THREAD_STATEMENT : SELECT_THREAD_REPLY_STATUS_STATEMENT;
        try (Connection connection = this.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            int threadIndex = 1;
            if (courseId != null){
                statement.setInt(1, courseId);
                threadIndex = 2;
            }
            statement.setInt(threadIndex, threadId);

            ResultSet result = statement.executeQuery();
            if (!result.next()) {
                return null;
            }

            return this.fromThreadResult(result);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Post> getPosts(int threadId) {
        try (Connection connection = this.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_POSTS_STATEMENT);
            statement.setInt(1, threadId);

            ResultSet result = statement.executeQuery();

            List<Post> posts = new ArrayList<>();
            while (result.next()) {
                posts.add(this.fromPostResult(result));
            }

            return posts;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Thread makeThread(String title, int courseId, int folderId, String tag, boolean anonymous, String text) {
        if (title == null) {
            throw new IllegalArgumentException("Title can not be null!");
        }

        if (tag == null) {
            throw new IllegalArgumentException("Tag can not be null!");
        }

        try (Connection connection = this.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(INSERT_THREAD_STATEMENT, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, title);
            statement.setInt(2, courseId);
            statement.setInt(3, folderId);
            statement.setString(4, tag);

            statement.execute();

            ResultSet keys = statement.getGeneratedKeys();
            keys.next();
            int threadId = keys.getInt(1);

            this.makePost(threadId, true, anonymous, text);

            return new Thread(threadId, title, courseId, folderId, tag, Thread.NOT_ANSWERED);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Post makePost(int threadId, boolean isRoot, boolean anonymous, String text) {
        if (text == null) {
            throw new IllegalArgumentException("Text can not be null!");
        }

        User user = this.getApp().getAuthController().getCurrentUser();
        if (user == null) {
            throw new IllegalStateException("You have to be logged in to do this!");
        }

        LocalDateTime now = LocalDateTime.now();

        try (Connection connection = this.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(INSERT_POST_STATEMENT, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, threadId);
            statement.setBoolean(2, isRoot);
            statement.setBoolean(3, anonymous);
            statement.setTimestamp(4, Timestamp.valueOf(now));
            statement.setString(5, text);
            statement.setString(6, user.getEmail());

            statement.execute();

            ResultSet generated = statement.getGeneratedKeys();
            generated.next();
            int postId = generated.getInt(1);

            return new Post(postId, threadId, isRoot, anonymous, now, text, user.getEmail());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Thread fromThreadResult(ResultSet result) throws SQLException {
        return new Thread(
                result.getInt("ThreadId"),
                result.getString("Title"),
                result.getInt("CourseId"),
                result.getInt("FolderId"),
                result.getString("Tag"),
                result.getInt("Answered")
        );
    }

    public Post fromPostResult(ResultSet result) throws SQLException {
        return new Post(
                result.getInt("PostId"),
                result.getInt("ThreadId"),
                result.getBoolean("IsRoot"),
                result.getBoolean("Anonymous"),
                result.getTimestamp("PostedAt").toLocalDateTime(),
                result.getString("Text"),
                result.getString("CreatedByUser")
        );
    }
}
