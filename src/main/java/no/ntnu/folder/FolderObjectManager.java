package no.ntnu.folder;


import no.ntnu.App;
import no.ntnu.mysql.ActiveDomainObjectManager;
import no.ntnu.mysql.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class FolderObjectManager extends ActiveDomainObjectManager {
    private ConnectionManager connectionManager;
    private final String INSERT_FOLDER = "INSERT INTO Folder(Name, CourseId, ParentFolderId) VALUES(?, ?, ?);";

    public FolderObjectManager(App app) {
        super(app);
    }

    public void createFolder(String name, int courseId, Integer parentFolderId) throws SQLException, NullPointerException {
        if (name == null) {
            throw new NullPointerException("Please provide a name!");
        }

        try (Connection connection = connectionManager.getConnection(); PreparedStatement statement = connection.prepareStatement(INSERT_FOLDER, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.setInt(2, courseId);

            if (parentFolderId != null) {
                statement.setInt(3, parentFolderId);
            } else {
                statement.setNull(3, Types.INTEGER);
            }

            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();
            throw e;
        }
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
