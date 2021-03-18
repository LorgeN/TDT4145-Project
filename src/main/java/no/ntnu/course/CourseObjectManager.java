package no.ntnu.course;

import no.ntnu.App;
import no.ntnu.auth.User;
import no.ntnu.course.command.CreateCourseCommand;
import no.ntnu.course.command.InviteUserCommand;
import no.ntnu.course.command.ViewCoursesCommand;
import no.ntnu.mysql.ActiveDomainObjectManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseObjectManager extends ActiveDomainObjectManager {

    private static final String INSERT_COURSE_STATEMENT = "INSERT INTO course(Name, Term, AllowAnonymous)" +
            " VALUES (?, ?, ?);";
    private static final String SELECT_COURSES_BY_NAME_STATEMENT = "SELECT * FROM course WHERE course.Name = ?;";
    private static final String SELECT_INSTRUCTOR_COURSES_BY_NAME_STATEMENT = "SELECT * FROM course C NATURAL" +
            "  JOIN participant P WHERE C.Name = ? AND P.User = ? AND P.IsInstructor = TRUE;";
    private static final String INSERT_PARTICIPANT_STATEMENT = "INSERT INTO participant(User, CourseId, IsInstructor)" +
            " VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE IsInstructor=VALUES(IsInstructor);";
    private static final String SELECT_USER_COURSES_STATEMENT = "SELECT p.IsInstructor, c.* FROM " +
            "participant p NATURAL JOIN course c WHERE p.User = ?;";

    public CourseObjectManager(App app) {
        super(app);

        app.getRunner().registerCommand("createcourse", new CreateCourseCommand(app));
        app.getRunner().registerCommand("viewcourses", new ViewCoursesCommand(app));
        app.getRunner().registerCommand("inviteuser", new InviteUserCommand(app));
    }

    public Course createCourse(String name, String term, boolean allowAnonymous) {
        if (!this.getApp().getAuthController().isAuthenticated()) {
            throw new IllegalStateException("You have to be authenticated to do this!");
        }

        if (name == null || name.length() > 64) {
            throw new IllegalArgumentException("Invalid name \"" + name + "\"! Can " +
                    "not be null and must be at most 64 characters");
        }

        if (term == null || term.length() > 32) {
            throw new IllegalArgumentException("Invalid term \"" + term + "\"! Can " +
                    "not be null and must be at most 32 characters");
        }

        try (Connection connection = this.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(INSERT_COURSE_STATEMENT, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.setString(2, term);
            statement.setBoolean(3, allowAnonymous);

            statement.execute();

            ResultSet generated = statement.getGeneratedKeys();
            generated.next();
            int courseId = generated.getInt(1);

            // Add the current user as an instructor in the created course
            User currentUser = this.getApp().getAuthController().getCurrentUser();
            this.addParticipant(currentUser.getEmail(), courseId, true);

            return new Course(courseId, name, term, allowAnonymous);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Course> getCoursesByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name can not be null!");
        }

        try (Connection connection = this.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_COURSES_BY_NAME_STATEMENT);
            statement.setString(1, name);

            ResultSet result = statement.executeQuery();
            List<Course> courses = new ArrayList<>();
            while (result.next()) {
                courses.add(this.fromResultSet(result));
            }

            return courses;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Course> getInstructorCourses(String user, String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name can not be null!");
        }

        if (user == null) {
            throw new IllegalArgumentException("User can not be null!");
        }

        try (Connection connection = this.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_INSTRUCTOR_COURSES_BY_NAME_STATEMENT);
            statement.setString(1, name);
            statement.setString(2, user);

            ResultSet result = statement.executeQuery();
            List<Course> courses = new ArrayList<>();
            while (result.next()) {
                courses.add(this.fromResultSet(result));
            }

            return courses;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CourseParticipant addParticipant(String user, int courseId, boolean isInstructor) {
        if (user == null) {
            throw new IllegalArgumentException("User can not be null!");
        }

        try (Connection connection = this.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(INSERT_PARTICIPANT_STATEMENT);
            statement.setString(1, user);
            statement.setInt(2, courseId);
            statement.setBoolean(3, isInstructor);

            statement.execute();

            return new CourseParticipant(user, courseId, isInstructor);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<Course, Boolean> getCourses(String user) {
        if (user == null) {
            throw new IllegalArgumentException("User can not be null!");
        }

        try (Connection connection = this.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_USER_COURSES_STATEMENT);
            statement.setString(1, user);

            ResultSet result = statement.executeQuery();

            Map<Course, Boolean> map = new HashMap<>();

            while (result.next()) {
                Course course = this.fromResultSet(result);
                boolean instructor = result.getBoolean("IsInstructor");
                map.put(course, instructor);
            }

            return map;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Course fromResultSet(ResultSet result) throws SQLException {
        int courseId = result.getInt("CourseId");
        String name = result.getString("Name");
        String term = result.getString("Term");
        boolean allowAnonymous = result.getBoolean("AllowAnonymous");
        return new Course(courseId, name, term, allowAnonymous);
    }
}