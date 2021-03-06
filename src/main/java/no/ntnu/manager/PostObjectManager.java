package no.ntnu.manager;

import no.ntnu.App;
import no.ntnu.entity.Post;
import no.ntnu.entity.Thread;
import no.ntnu.entity.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Object manager for the {@link Thread} and {@link Post} entity classes.
 * <p>
 * {@inheritDoc}
 *
 * @see Thread
 * @see Post
 */
public class PostObjectManager extends ActiveDomainObjectManager {

    private static final String INSERT_THREAD_STATEMENT = "INSERT INTO Thread(Title, CourseId, FolderId, Tag) " +
            "VALUES (?, ?, ?, ?);";

    private static final String INSERT_POST_STATEMENT = "INSERT INTO Post(ThreadId, IsRoot, Anonymous, PostedAt, " +
            "Text, CreatedByUser) VALUES (?, ?, ?, ?, ?, ?);";

    private static final String GOOD_COMMENT_STATEMENT = "INSERT INTO GoodComment (User, PostId) VALUES(?, ?)";

    // Statement will enforce anonymous choice for the course
    private static final String SELECT_POSTS_STATEMENT = "SELECT P.PostId, P.ThreadId, " +
            "P.IsRoot, P.PostedAt, P.Text, P.CreatedByUser, " +
            "IF(P.Anonymous AND EXISTS(SELECT * " +
            "FROM Thread T " +
            "INNER JOIN Course C on T.CourseId = C.CourseId " +
            "WHERE AllowAnonymous = TRUE AND T.ThreadId = P.ThreadId), " +
            "TRUE, FALSE) AS Anonymous " +
            "FROM Post P " +
            "WHERE P.ThreadId=?;";

    private static final String SELECT_THREAD_REPLY_STATUS_STATEMENT = "SELECT *, " +
            "CASE " +
            "WHEN EXISTS(SELECT * FROM (Post P JOIN User U ON P.CreatedByUser = U.Email) JOIN Participant PP ON U.Email = PP.User WHERE PP.IsInstructor = TRUE AND P.ThreadId = T.ThreadId AND PP.CourseId = T.CourseId) THEN " + Thread.INSTRUCTOR_ANSWERED + " " +
            "WHEN (SELECT COUNT(*) FROM Post P3 WHERE P3.ThreadId = T.ThreadId) > 1 THEN " + Thread.ANSWERED + " " +
            "ELSE " + Thread.NOT_ANSWERED + " " +
            "END AS Answered " +
            "FROM Thread T WHERE T.ThreadId = ?;";

    private static final String SELECT_SINGLE_POST_STATEMENT = "SELECT * FROM Post WHERE PostId = ?";

    private static final String FIND_GOOD_COMMENT_FROM_USER_ON_POST_STATEMENT = "SELECT * FROM GoodComment " +
            "WHERE User = ? " +
            "AND PostId = ?";

    private static final String SEARCH_STRING = "SELECT * FROM Post P JOIN Thread T ON P.ThreadId = T.ThreadId " +
            "WHERE P.Text LIKE ? AND T.CourseId = ?";

    /**
     * {@inheritDoc}
     */
    public PostObjectManager(App app) {
        super(app);
    }

    /**
     * Searches for posts containing keyword as specified in usecase 4
     *
     * @param keyword include only posts with text containing this keyword
     */
    public List<Post> search(String keyword, int courseId) {
        try (Connection connection = this.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SEARCH_STRING);
            statement.setString(1, "%" + keyword + "%");
            statement.setInt(2, courseId);

            ResultSet results = statement.executeQuery();

            List<Post> posts = new ArrayList<>();
            while (results.next()) {
                posts.add(this.fromPostResult(results));
            }

            return posts;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets information about the {@link Thread thread} with the given thread ID.
     *
     * @param threadId The thread's ID
     * @return The thread
     */
    public Thread getThread(int threadId) {
        try (Connection connection = this.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_THREAD_REPLY_STATUS_STATEMENT);
            int threadIndex = 1;
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

    /**
     * Gets all {@link Post posts} in the {@link Thread thread} with the given ID.
     *
     * @param threadId The thread ID
     * @return The posts in the given thread
     */
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

    /**
     * Gets the {@link Post post} with the given ID
     *
     * @param postId The ID of the post
     * @return The post
     */
    public Post getPostById(int postId) {
        try (Connection connection = this.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_SINGLE_POST_STATEMENT);
            statement.setInt(1, postId);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return this.fromPostResult(result);
            } else {
                return null;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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

        User user = this.getApp().getUserManager().getCurrentUser();
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

    public void addGoodCommentToPost(Post post, User user) throws SQLException {
        try (Connection connection = this.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(GOOD_COMMENT_STATEMENT);
            statement.setString(1, user.getEmail());
            statement.setInt(2, post.getPostId());
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean hasAddedgoodCommentToPost(User user, Post post) {
        try (Connection connection = this.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FIND_GOOD_COMMENT_FROM_USER_ON_POST_STATEMENT);
            statement.setString(1, user.getEmail());
            statement.setInt(2, post.getPostId());
            ResultSet result = statement.executeQuery();
            return result.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

}
