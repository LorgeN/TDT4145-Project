package no.ntnu.folder;

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
    public String toString() {
        return "Folder{" +
            "name='" + name + '\'' +
            ", folderId=" + folderId +
            ", parentFolderId=" + parentFolderId +
            ", courseId=" + courseId +
            '}';
    }
}
