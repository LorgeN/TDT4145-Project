package no.ntnu.entity;

import java.util.Objects;

/**
 * Object representation of a Thread entity
 */
public class Thread {
    public static final int ANSWERED = 0;
    public static final int NOT_ANSWERED = 1;
    public static final int INSTRUCTOR_ANSWERED = 2;

    private final int threadId;
    private final String title;
    private final int courseId;
    private final int folderId;
    private final String tag;
    private final int answered;

    public Thread(int threadId, String title, int courseId, int folderId, String tag, int answered) {
        this.threadId = threadId;
        this.title = title;
        this.courseId = courseId;
        this.folderId = folderId;
        this.tag = tag;
        this.answered = answered;
    }

    public int getThreadId() {
        return threadId;
    }

    public String getTitle() {
        return title;
    }

    public int getCourseId() {
        return courseId;
    }

    public int getFolderId() {
        return folderId;
    }

    public String getTag() {
        return tag;
    }

    public int getAnswered() {
        return answered;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Thread thread = (Thread) o;
        return getThreadId() == thread.getThreadId()
                && getCourseId() == thread.getCourseId()
                && getFolderId() == thread.getFolderId()
                && Objects.equals(getTitle(), thread.getTitle())
                && Objects.equals(getTag(), thread.getTag());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getThreadId(), getTitle(), getCourseId(), getFolderId(), getTag());
    }

    @Override
    public String toString() {
        return "Thread{" +
                "threadId=" + threadId +
                ", title='" + title + '\'' +
                ", courseId=" + courseId +
                ", folderId=" + folderId +
                ", tag='" + tag + '\'' +
                '}';
    }
}
