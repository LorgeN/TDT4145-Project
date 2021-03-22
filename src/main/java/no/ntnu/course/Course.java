package no.ntnu.course;

import java.util.Objects;

public class Course {

    private int courseId;
    private String name;
    private String term;
    private boolean allowAnonymous;

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
