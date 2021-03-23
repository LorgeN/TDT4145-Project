package no.ntnu.search.command;

import no.ntnu.App;
import no.ntnu.auth.command.ProtectedCommand;
import no.ntnu.course.Course;
import no.ntnu.posts.Post;
import no.ntnu.search.SearchObjectManager;

import java.util.List;

public class SearchCommand extends ProtectedCommand {
    public SearchCommand(App app) {
        super(app);
    }

    @Override
    protected void protectedExecute(String label, String[] args) {
       if (args.length < 1){
           System.out.println("Please specify a keyword");
       }

       SearchObjectManager controller = this.getApp().getSearchController();
       String keyword = args[0];
       Course course = this.getApp().getCourseObjectManager().getSelectedCourse();
       if (course == null){
           System.out.println("You have to select a course to search for posts!");
           return;
       }
       List<Post> posts = controller.search(keyword, course.getCourseId(), this.getApp().getPostObjectManager());
       StringBuilder result = new StringBuilder("Posts containing '" + keyword + "':\n");
       for (int i = 0; i < posts.size(); i++){
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
