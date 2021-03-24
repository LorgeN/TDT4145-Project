package no.ntnu.entity;

import java.util.Objects;

/**
 * Object representation of a Course entity
 */
public class Course {

    private int courseId;
    private final String name;
    private final String term;
    private final boolean allowAnonymous;

    public Course(int courseId, String name, String term, boolean allowAnonymous) {
        this.courseId = courseId;
        this.name = name;
        this.term = term;
        this.allowAnonymous = allowAnonymous;
    }

    public Course(String name, String term, boolean allowAnonymous) {
        this.name = name;
        this.term = term;
        this.allowAnonymous = allowAnonymous;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getName() {
        return name;
    }

    public String getTerm() {
        return term;
    }

    public boolean isAllowAnonymous() {
        return allowAnonymous;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Course course = (Course) o;
        return getCourseId() == course.getCourseId() && isAllowAnonymous() == course.isAllowAnonymous() && Objects.equals(getName(), course.getName()) && Objects.equals(getTerm(), course.getTerm());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCourseId(), getName(), getTerm(), isAllowAnonymous());
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", name='" + name + '\'' +
                ", term='" + term + '\'' +
                ", allowAnonymous=" + allowAnonymous +
                '}';
    }
}
