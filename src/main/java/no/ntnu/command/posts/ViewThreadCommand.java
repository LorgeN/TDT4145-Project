package no.ntnu.command.posts;

import no.ntnu.App;
import no.ntnu.entity.User;
import no.ntnu.command.auth.ProtectedCommand;
import no.ntnu.entity.Course;
import no.ntnu.entity.Post;
import no.ntnu.manager.PostObjectManager;
import no.ntnu.entity.Thread;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ViewThreadCommand extends ProtectedCommand {
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_WHITE = "\u001B[37m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_RESET = "\u001B[0m";

    public ViewThreadCommand(App app) {
        super(app);
    }

    @Override
    public String getUsage() {
        return "<threadId>";
    }

    @Override
    public String getDescription() {
        return "View a thread";
    }

    @Override
    protected void protectedExecute(String label, String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a thread ID!");
            return;
        }

        int threadId;

        try {
            threadId = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number!");
            return;
        }


        PostObjectManager manager = this.getApp().getPostObjectManager();
        Thread thread = manager.getThread(threadId);
        if (thread == null) {
            System.out.println("No thread with that ID exists!");
            return;
        }

        List<Post> posts = manager.getPosts(threadId);

        User user = this.getApp().getAuthController().getCurrentUser();

        this.getApp().getStatisticsManager().readPost(user.getEmail(), posts.stream()
                .map(Post::getPostId).collect(Collectors.toList()));

        // Change thread color depending on whether or not someone has answered it and if that someone was an instructor
        String ansiColor = ANSI_RED;
        String color = "Red";

        if (thread.getAnswered() == Thread.ANSWERED){
            ansiColor = ANSI_BLUE;
            color = "Blue";
        } else if (thread.getAnswered() == Thread.INSTRUCTOR_ANSWERED) {
            ansiColor = ANSI_GREEN;
            color = "Green";
        }

        System.out.println(ansiColor + "Thread " + thread.getThreadId() + ": " + thread.getTitle() + " (" + color + ")" + ANSI_RESET);
        System.out.println(" Folder: " + thread.getFolderId()); // This could be replaced by the pretty name?
        System.out.println(" Tag: " + thread.getTag());

        // Simple assumption that we want to read oldest posts at the top
        posts.stream()
                .sorted(Comparator.comparing(Post::getPostedAt))
                .forEach(post -> System.out.println((post.isRoot() ? "(ROOT) " : "") +
                        (post.isAnonymous() ? "Anonym" : post.getCreatedByUser()) + " @ " +
                        DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(post.getPostedAt()) + " (" + post.getPostId()
                        + ") > " + post.getText()));

    }
}
