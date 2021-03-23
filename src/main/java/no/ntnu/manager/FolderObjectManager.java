package no.ntnu.manager;


import no.ntnu.App;
import no.ntnu.entity.Folder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FolderObjectManager extends ActiveDomainObjectManager {
    private static final String INSERT_FOLDER = "INSERT INTO Folder(Name, CourseId, ParentFolderId) VALUES(?, ?, ?);";
    private static final String SELECT_FOLDER = "SELECT * FROM Folder WHERE Name = ? AND CourseId = ?;";


    public FolderObjectManager(App app) {
        super(app);
    }

    /**
     * Creates a folder with the given values
     *
     * @param name           the name of the folder
     * @param courseId       the id of the course the folder belongs to
     * @param parentFolderId the id of the parent folder, can be null.
     */
    public void createFolder(String name, int courseId, Integer parentFolderId) {
        if (name == null) {
            throw new IllegalArgumentException("Please provide a name!");
        }

        try (Connection connection = this.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_FOLDER, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.setInt(2, courseId);

            if (parentFolderId != null) {
                statement.setInt(3, parentFolderId);
            } else {
                statement.setNull(3, Types.INTEGER);
            }

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get all folders with a given name
     *
     * @param courseId id of the course to look within for folders
     * @param name     name to search for
     * @return a list of folders with the given name
     */
    public List<Folder> getFoldersByName(int courseId, String name) {
        try (Connection connection = this.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_FOLDER)) {
            statement.setString(1, name);
            statement.setInt(2, courseId);

            ResultSet result = statement.executeQuery();
            List<Folder> folders = new ArrayList<>();
            while (result.next()) {
                folders.add(fromResultSet(result));
            }

            return folders;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Create a folder from the given ResultSet
     *
     * @param result the set to make the folder from
     * @return newly created folder
     * @throws SQLException if the ResultSet does not contain the needed columns
     */
    private Folder fromResultSet(ResultSet result) throws SQLException {
        String name = result.getString("Name");
        int courseId = result.getInt("CourseId");
        Integer parentFolderId = result.getInt("ParentFolderId");
        int folderId = result.getInt("FolderId");
        Folder folder = new Folder(name, courseId, parentFolderId);
        folder.setFolderId(folderId);
        return folder;
    }
}
