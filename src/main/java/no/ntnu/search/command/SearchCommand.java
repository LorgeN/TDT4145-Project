package no.ntnu.search.command;

import no.ntnu.App;
import no.ntnu.auth.command.ProtectedCommand;
import no.ntnu.course.Course;
import no.ntnu.search.SearchObjectManager;

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
       controller.search(keyword, course.getCourseId());
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
