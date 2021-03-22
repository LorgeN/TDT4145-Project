package no.ntnu.search;

import no.ntnu.mysql.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchController {
    private ConnectionManager connectionManager;
    private static final String SEARCH_STRING = "SELECT PostId FROM Post WHERE Text LIKE ?";

    /**
     * Searches for posts containing keyword as specified in usecase 4
     * @param keyword include only posts with text containing this keyword
     */
    public void search(String keyword){
        try (Connection connection = this.connectionManager.getConnection(); PreparedStatement  statement = connection.prepareStatement(SEARCH_STRING)){
            keyword = "%" + keyword + "%";
            statement.setString(1, keyword);
            ResultSet results = statement.executeQuery();

            while (results.next()){
                int postId = results.getInt("PostId");
                System.out.println(postId);
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
}
