package no.ntnu.entity;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Object representation of a Post entity
 */
public class Post {

    private final int postId;
    private final int threadId;
    private final boolean isRoot;
    private final boolean anonymous;
    private final LocalDateTime postedAt;
    private final String text;
    private final String createdByUser;

    public Post(int postId, int threadId, boolean isRoot, boolean anonymous, LocalDateTime postedAt, String text, String createdByUser) {
        this.postId = postId;
        this.threadId = threadId;
        this.isRoot = isRoot;
        this.anonymous = anonymous;
        this.postedAt = postedAt;
        this.text = text;
        this.createdByUser = createdByUser;
    }

    public int getPostId() {
        return postId;
    }

    public int getThreadId() {
        return threadId;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public LocalDateTime getPostedAt() {
        return postedAt;
    }

    public String getText() {
        return text;
    }

    public String getCreatedByUser() {
        return createdByUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Post post = (Post) o;
        return getPostId() == post.getPostId()
                && getThreadId() == post.getThreadId()
                && isRoot() == post.isRoot()
                && isAnonymous() == post.isAnonymous()
                && Objects.equals(getPostedAt(), post.getPostedAt())
                && Objects.equals(getText(), post.getText())
                && Objects.equals(getCreatedByUser(), post.getCreatedByUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPostId(), getThreadId(), isRoot(), isAnonymous(), getPostedAt(), getText(), getCreatedByUser());
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", threadId=" + threadId +
                ", isRoot=" + isRoot +
                ", anonymous=" + anonymous +
                ", postedAt=" + postedAt +
                ", text='" + text + '\'' +
                ", createdByUser='" + createdByUser + '\'' +
                '}';
    }
}
