package no.ntnu.command.posts;

import no.ntnu.App;
import no.ntnu.entity.User;
import no.ntnu.command.auth.ProtectedCommand;
import no.ntnu.entity.Post;
import no.ntnu.manager.PostObjectManager;

import java.sql.SQLException;

public class GoodCommentCommand extends ProtectedCommand {
    public GoodCommentCommand(App app) {
        super(app);
    }

    @Override
    protected void protectedExecute(String label, String[] args) {
        User user = this.getApp().getUserManager().getCurrentUser();
        PostObjectManager postObjectManager = this.getApp().getPostObjectManager();
        Post post = postObjectManager.getPostById(Integer.parseInt(args[0]));
        if (post == null) {
            System.out.println("The post does not exist");
            return;
        }
        if (postObjectManager.hasAddedgoodCommentToPost(user, post)) {
            System.out.println("You have already added GoodComment to this post");
            return;
        }

        try {
            postObjectManager.addGoodCommentToPost(post, user);
            System.out.println("GoodComment added to post: " + post);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @Override
    public String getUsage() {
        return "<PostId>";
    }

    @Override
    public String getDescription() {
        return "Adds a 'GoodComment' mark to a post";
    }
}
