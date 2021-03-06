package no.ntnu.entity;

import java.util.Objects;

/**
 * Object representation of a CourseParticipant entity
 */
public class CourseParticipant {

    private final String user;
    private final int courseId;
    private final boolean instructor;

    public CourseParticipant(String user, int courseId, boolean instructor) {
        this.user = user;
        this.courseId = courseId;
        this.instructor = instructor;
    }

    public String getUser() {
        return user;
    }

    public int getCourseId() {
        return courseId;
    }

    public boolean isInstructor() {
        return instructor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CourseParticipant that = (CourseParticipant) o;
        return getCourseId() == that.getCourseId() && isInstructor() == that.isInstructor() && Objects.equals(getUser(), that.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser(), getCourseId(), isInstructor());
    }

    @Override
    public String toString() {
        return "CourseParticipant{" +
                "user='" + user + '\'' +
                ", courseId=" + courseId +
                ", instructor=" + instructor +
                '}';
    }
}
