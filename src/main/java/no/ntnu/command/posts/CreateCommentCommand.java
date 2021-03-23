package no.ntnu.command.posts;

import no.ntnu.App;
import no.ntnu.command.auth.ProtectedCommand;
import no.ntnu.entity.Post;
import no.ntnu.manager.PostObjectManager;
import no.ntnu.entity.Thread;
import no.ntnu.util.CommandUtil;

public class CreateCommentCommand extends ProtectedCommand {

    public CreateCommentCommand(App app) {
        super(app);
    }

    @Override
    protected void protectedExecute(String label, String[] args) {
        if (args.length == 0) {
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

        boolean anonymous = false;
        if (args.length > 1) {
            anonymous = Boolean.parseBoolean(args[2]);
        }

        PostObjectManager manager = this.getApp().getPostObjectManager();
        Thread thread = manager.getThread(threadId);

        if (thread == null) {
            System.out.println("No thread with that ID exists!");
            return;
        }

        String text = CommandUtil.acceptInput("Enter comment:");

        Post post = manager.makePost(thread.getThreadId(), false, anonymous, text);
        System.out.println("Comment " + post.getPostId() + " added to thread "  + thread.getThreadId() + "!");
    }

    @Override
    public String getUsage() {
        return "<threadId> [anonymous]";
    }

    @Override
    public String getDescription() {
        return "Add a comment to a thread";
    }
}
