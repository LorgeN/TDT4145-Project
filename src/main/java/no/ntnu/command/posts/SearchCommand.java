package no.ntnu.command.posts;

import no.ntnu.App;
import no.ntnu.command.auth.ProtectedCommand;
import no.ntnu.entity.Course;
import no.ntnu.entity.Post;
import no.ntnu.manager.PostObjectManager;
import no.ntnu.manager.SearchObjectManager;

import java.util.List;

public class SearchCommand extends ProtectedCommand {
    public SearchCommand(App app) {
        super(app);
    }

    @Override
    protected void protectedExecute(String label, String[] args) {
        if (args.length < 1) {
            System.out.println("Please specify a keyword");
        }

        PostObjectManager manager = this.getApp().getPostObjectManager();
        String keyword = args[0];
        Course course = this.getApp().getCourseObjectManager().getSelectedCourse();
        if (course == null) {
            System.out.println("You have to select a course to search for posts!");
            return;
        }

        List<Post> posts = manager.search(keyword, course.getCourseId());
        StringBuilder result = new StringBuilder("Posts containing '" + keyword + "':\n");
        for (int i = 0; i < posts.size(); i++) {
            Post post = posts.get(i);
            result.append("\t").append(i + 1).append(". PostId: ").append(post.getPostId()).append(", Text: ").append(post.getText());
        }

        System.out.println(result);
    }

    @Override
    public String getUsage() {
        return "<keyword>";
    }

    @Override
    public String getDescription() {
        return "Gets all posts from current course where text includes keyword";
    }
}
