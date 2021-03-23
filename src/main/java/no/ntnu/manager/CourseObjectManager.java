package no.ntnu.manager;

import no.ntnu.App;
import no.ntnu.entity.Course;
import no.ntnu.entity.CourseParticipant;
import no.ntnu.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Object manager for the {@link Course} and {@link CourseParticipant} entity classes.
 * Maintains a state for the currently selected course.
 * <p>
 * {@inheritDoc}
 *
 * @see Course
 * @see CourseParticipant
 */
public class CourseObjectManager extends ActiveDomainObjectManager {

    private static final String INSERT_COURSE_STATEMENT = "INSERT INTO course(Name, Term, AllowAnonymous)" +
            " VALUES (?, ?, ?);";

    private static final String SELECT_COURSES_BY_NAME_STATEMENT = "SELECT * FROM course WHERE course.Name = ?;";

    private static final String SELECT_INSTRUCTOR_COURSES_BY_NAME_STATEMENT = "SELECT * FROM course C NATURAL" +
            " JOIN participant P WHERE C.Name = ? AND P.User = ? AND P.IsInstructor = TRUE;";

    private static final String INSERT_PARTICIPANT_STATEMENT = "INSERT INTO participant(User, CourseId, IsInstructor)" +
            " VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE IsInstructor=VALUES(IsInstructor);";

    private static final String SELECT_USER_COURSES_STATEMENT = "SELECT p.IsInstructor, c.* FROM " +
            "participant p NATURAL JOIN course c WHERE p.User = ?;";

    private Course selectedCourse;

    /**
     * {@inheritDoc}
     */
    public CourseObjectManager(App app) {
        super(app);
    }

    /**
     * Creates a course with the given parameters
     *
     * @param name           the name of the course
     * @param term           the term the course is taking place
     * @param allowAnonymous whether or not this course allows anonymous posts
     * @return the newly created course
     */
    public Course createCourse(String name, String term, boolean allowAnonymous) {
        if (!this.getApp().getUserManager().isAuthenticated()) {
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
            User currentUser = this.getApp().getUserManager().getCurrentUser();
            this.addParticipant(currentUser.getEmail(), courseId, true);

            return new Course(courseId, name, term, allowAnonymous);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return The currently selected course
     */
    public Course getSelectedCourse() {
        return selectedCourse;
    }

    /**
     * Set the currently selected course
     *
     * @param course The updated selected {@link Course course}.
     */
    public void selectCourse(Course course) {
        this.selectedCourse = course;
    }

    /**
     * Gets all courses with a given name
     *
     * @param name the name of the courses to return
     * @return list of courses with the name
     */
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

    /**
     * Select all courses with a specific name where a given user is an instructor
     *
     * @param user the instructor
     * @param name the name of of the course
     * @return list of courses with the name where the user is an instructor
     */
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

    /**
     * Adds a participant to a course. If the user is already a participant,
     * their isInstructor status will be updated to what is provided.
     *
     * @param user         the user to add as a participant
     * @param courseId     the id of the course the user participates in
     * @param isInstructor whether or not the user if an instructor in the course
     * @return CourseParticipation object signifying the participation
     */
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

    /**
     * Get all the courses a given user participates in
     *
     * @param user the user that participates
     * @return Map with course as key and boolean signifying whether or not the user is an instructor as value
     */
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

    /**
     * Creates a course from a ResultSet
     *
     * @param result the ResultSet to create the Course from
     * @return the newly created course
     * @throws SQLException if one of the columns needed does not exist in the result
     */
    public Course fromResultSet(ResultSet result) throws SQLException {
        int courseId = result.getInt("CourseId");
        String name = result.getString("Name");
        String term = result.getString("Term");
        boolean allowAnonymous = result.getBoolean("AllowAnonymous");
        return new Course(courseId, name, term, allowAnonymous);
    }
}
