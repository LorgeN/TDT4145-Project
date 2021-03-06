package no.ntnu.entity;

import java.util.Objects;

/**
 * Object representation of a Tag entity
 */
public class Tag {

    private final int courseId;
    private final String name;

    public Tag(int courseId, String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name can not be null!");
        }

        this.courseId = courseId;
        this.name = name;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Tag tag = (Tag) o;
        return getCourseId() == tag.getCourseId() &&
                Objects.equals(getName(), tag.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCourseId(), getName());
    }

    @Override
    public String toString() {
        return "Tag{" +
                "courseId=" + courseId +
                ", name='" + name + '\'' +
                '}';
    }
}
