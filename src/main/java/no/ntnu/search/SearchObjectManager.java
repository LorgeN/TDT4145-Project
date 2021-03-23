package no.ntnu.search;

import no.ntnu.App;
import no.ntnu.mysql.ActiveDomainObjectManager;
import no.ntnu.mysql.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchObjectManager extends ActiveDomainObjectManager {
    private ConnectionManager connectionManager;
    private static final String SEARCH_STRING = "SELECT PostId FROM Post P JOIN Thread T ON P.ThreadId = T.ThreadId WHERE P.Text LIKE ? AND T.CourseId = ?";

    public SearchObjectManager(App app) {
        super(app);
    }

    /**
     * Searches for posts containing keyword as specified in usecase 4
     * @param keyword include only posts with text containing this keyword
     */
    public void search(String keyword, int courseId){
        try (Connection connection = this.connectionManager.getConnection(); PreparedStatement  statement = connection.prepareStatement(SEARCH_STRING)){
            statement.setString(1, "%" + keyword + "%");
            statement.setInt(2, courseId);
            ResultSet results = statement.executeQuery();

            int index = 1;
            System.out.println("Posts containing '" + keyword + "': ");
            while (results.next()){
                int postId = results.getInt("PostId");
                System.out.println("\t" + index + ". PostId: " + postId);
                index++;
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
}
