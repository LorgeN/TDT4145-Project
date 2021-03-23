package no.ntnu.search;

import no.ntnu.App;
import no.ntnu.mysql.ActiveDomainObjectManager;
import no.ntnu.mysql.ConnectionManager;
import no.ntnu.posts.Post;
import no.ntnu.posts.PostObjectManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchObjectManager extends ActiveDomainObjectManager {
    private ConnectionManager connectionManager;
    private static final String SEARCH_STRING = "SELECT * FROM Post P JOIN Thread T ON P.ThreadId = T.ThreadId WHERE P.Text LIKE ? AND T.CourseId = ?";

    public SearchObjectManager(App app) {
        super(app);
    }

    /**
     * Searches for posts containing keyword as specified in usecase 4
     * @param keyword include only posts with text containing this keyword
     */
    public List<Post> search(String keyword, int courseId, PostObjectManager postObjectManager){
        try (Connection connection = this.connectionManager.getConnection(); PreparedStatement  statement = connection.prepareStatement(SEARCH_STRING)){
            statement.setString(1, "%" + keyword + "%");
            statement.setInt(2, courseId);
            ResultSet results = statement.executeQuery();

            List<Post> posts = new ArrayList<>();
            while (results.next()){
               posts.add(postObjectManager.fromPostResult(results));
            }

            return posts;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
}
