package no.ntnu.tags;

import no.ntnu.App;
import no.ntnu.tags.command.ViewTagCommand;
import no.ntnu.mysql.ActiveDomainObjectManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TagObjectManager extends ActiveDomainObjectManager {

    private static final String SELECT_TAGS_STATEMENT = "SELECT * FROM tag;";
    private static final String SELECT_COURSE_TAGS_STATEMENT = "SELECT * FROM tag WHERE CourseId=?;";
    private static final String INSERT_TAG_STATEMENT = "INSERT INTO tag VALUES (?, ?);";
    private static final String DELETE_TAG_STATEMENT = "DELETE FROM tag WHERE CourseId = ? AND Name = ?;";

    public TagObjectManager(App app) {
        super(app);

        app.getRunner().registerCommand("viewtags", new ViewTagCommand(app));
    }

    public List<Tag> getTags() {
        try (Connection connection = this.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_TAGS_STATEMENT);

            List<Tag> tags = new ArrayList<>();

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                tags.add(this.fromResultSet(result));
            }

            return tags;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Tag> getCourseTags(int courseId) {
        try (Connection connection = this.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_COURSE_TAGS_STATEMENT);
            statement.setInt(1, courseId);

            List<Tag> tags = new ArrayList<>();

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                tags.add(this.fromResultSet(result));
            }

            return tags;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveTag(Tag tag) {
        try (Connection connection = this.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(INSERT_TAG_STATEMENT);
            statement.setInt(1, tag.getCourseId());
            statement.setString(2, tag.getName());

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTag(Tag tag) {
        try (Connection connection = this.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(DELETE_TAG_STATEMENT);
            statement.setInt(1, tag.getCourseId());
            statement.setString(2, tag.getName());

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Tag fromResultSet(ResultSet set) throws SQLException {
        int courseId = set.getInt("courseId");
        String name = set.getString("name");
        return new Tag(courseId, name);
    }
}
