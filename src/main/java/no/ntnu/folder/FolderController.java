package no.ntnu.folder;


import no.ntnu.mysql.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FolderController {
    private ConnectionManager connectionManager;

    public FolderController(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public void createFolder(String name, int courseId, Integer parentFolderId) throws SQLException, NullPointerException {
        if (name == null) {
            throw new NullPointerException("Please provide a name!");
        }
        Folder folder = new Folder(name, courseId, parentFolderId);
        Connection conn = connectionManager.getConnection();
        folder.save(conn);
    }

    public List<Folder> getFoldersByName(String name) {
        String queryString = "SELECT * FROM Folder WHERE Name = ?";
        try (PreparedStatement statement = connectionManager.getConnection().prepareStatement(queryString)) {
            statement.setString(1, name);
            ResultSet result = statement.executeQuery();
            List<Folder> folders = new ArrayList<>();
            while (result.next()){
                folders.add(fromResultSet(result));
            }

            return folders;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Folder fromResultSet(ResultSet result) throws SQLException {
        String name = result.getString("Name");
        int courseId = result.getInt("CourseId");
        Integer parentFolderId = result.getInt("ParentFolderId");
        int folderId = result.getInt("FolderId");
        Folder folder = new Folder(name, courseId, parentFolderId);
        folder.setFolderId(folderId);
        return folder;
    }


    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
}
