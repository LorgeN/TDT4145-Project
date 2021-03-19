package no.ntnu.folder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

public class Folder {
    private String name;
    private int folderId;
    private Integer parentFolderId;
    private int courseId;

    public Folder(String name, int courseId, Integer parentFolderId) {
        this.name = name;
        this.parentFolderId = parentFolderId;
        this.courseId = courseId;
    }

    public void save(Connection connection) throws SQLException {
        String queryString = "INSERT INTO Folder(Name, CourseId, ParentFolderId) VALUES(?, ?, ?);";

        try (PreparedStatement statement = connection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.setInt(2, courseId);

            if (parentFolderId != null) {
                statement.setInt(3, parentFolderId);
            } else {
                statement.setNull(3, Types.INTEGER);
            }

            statement.executeUpdate();
            ResultSet set = statement.getGeneratedKeys();
            set.next();
            folderId = set.getInt(1);
        } catch (SQLException e){
           e.printStackTrace();
           throw e;
        }
    }

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    @Override
    public String toString() {
        return "Folder{" +
            "name='" + name + '\'' +
            ", folderId=" + folderId +
            ", parentFolderId=" + parentFolderId +
            ", courseId=" + courseId +
            '}';
    }
}
