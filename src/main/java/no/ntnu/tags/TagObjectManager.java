package no.ntnu.tags;

import no.ntnu.App;
import no.ntnu.course.Course;
import no.ntnu.course.CourseObjectManager;
import no.ntnu.mysql.ActiveDomainObjectManager;
import no.ntnu.tags.command.CreateTagCommand;
import no.ntnu.tags.command.ViewTagsCommand;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagObjectManager extends ActiveDomainObjectManager {

    private static final String SELECT_TAGS_STATEMENT = "SELECT tag.Name AS tag, course.* FROM tag INNER JOIN course ON tag.CourseId = course.CourseId;";
    private static final String SELECT_COURSE_TAGS_STATEMENT = "SELECT * FROM tag WHERE CourseId=? AND Name=?;";
    private static final String INSERT_TAG_STATEMENT = "INSERT INTO tag VALUES (?, ?);";
    private static final String DELETE_TAG_STATEMENT = "DELETE FROM tag WHERE CourseId = ? AND Name = ?;";

    public TagObjectManager(App app) {
        super(app);

        app.getRunner().registerCommand("viewtags", new ViewTagsCommand(app));
        app.getRunner().registerCommand("createtag", new CreateTagCommand(app));
    }

    public Map<Course, List<Tag>> getTags() {
        CourseObjectManager courseManager = this.getApp().getCourseObjectManager();

        try (Connection connection = this.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_TAGS_STATEMENT);

            Map<Course, List<Tag>> tags = new HashMap<>();

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Course course = courseManager.fromResultSet(result);
                Tag tag = new Tag(course.getCourseId(), result.getString("tag"));

                tags.compute(course, (c, courseTags) -> {
                    if (courseTags == null) {
                        courseTags = new ArrayList<>();
                    }

                    courseTags.add(tag);
                    return courseTags;
                });
            }

            return tags;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Tag> getCourseTags(int courseId, String name) {
        try (Connection connection = this.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_COURSE_TAGS_STATEMENT);
            statement.setInt(1, courseId);
            statement.setString(2, name);

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

    public Tag createTag(int courseId, String name) {
        try (Connection connection = this.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(INSERT_TAG_STATEMENT);
            statement.setInt(1, courseId);
            statement.setString(2, name);

            statement.execute();

            return new Tag(courseId, name);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
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
