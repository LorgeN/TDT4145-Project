package no.ntnu.posts;

import no.ntnu.App;
import no.ntnu.auth.User;
import no.ntnu.mysql.ActiveDomainObjectManager;

import java.sql.*;
import java.time.LocalDateTime;

public class PostObjectManager extends ActiveDomainObjectManager {

    private static final String INSERT_THREAD_STATEMENT = "INSERT INTO thread(Title, CourseId, FolderId, Tag) " +
            "VALUES (?, ?, ?, ?);";
    private static final String INSERT_POST_STATEMENT = "INSERT INTO post(ThreadId, IsRoot, Anonymous, PostedAt, " +
            "Text, CreatedByUser) VALUES (?, ?, ?, ?, ?, ?);";

    public PostObjectManager(App app) {
        super(app);
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

            return new Thread(threadId, title, courseId, folderId, tag);
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
}
