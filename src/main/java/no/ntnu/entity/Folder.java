package no.ntnu.entity;

import java.util.Objects;

/**
 * Object representation of a Folder entity
 */
public class Folder {
    private final String name;
    private int folderId;
    private final Integer parentFolderId;
    private final int courseId;

    public Folder(String name, int courseId, Integer parentFolderId) {
        this.name = name;
        this.parentFolderId = parentFolderId;
        this.courseId = courseId;
    }

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Folder folder = (Folder) o;
        return getFolderId() == folder.getFolderId()
                && courseId == folder.courseId
                && Objects.equals(name, folder.name)
                && Objects.equals(parentFolderId, folder.parentFolderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, getFolderId(), parentFolderId, courseId);
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
