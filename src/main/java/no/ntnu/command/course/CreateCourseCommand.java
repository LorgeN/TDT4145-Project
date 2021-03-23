package no.ntnu.command.course;

import no.ntnu.App;
import no.ntnu.command.auth.ProtectedCommand;
import no.ntnu.entity.Course;
import no.ntnu.manager.CourseObjectManager;

public class CreateCourseCommand extends ProtectedCommand {

    public CreateCourseCommand(App app) {
        super(app);
    }

    @Override
    protected void protectedExecute(String label, String[] args) {
        if (args.length < 3) {
            System.out.println("Please provide name, term and allowAnonymous!");
            return;
        }

        String name = args[0];
        String term = args[1];
        boolean allowAnonymous = Boolean.parseBoolean(args[2]);

        CourseObjectManager manager = this.getApp().getCourseObjectManager();
        Course course = manager.createCourse(name, term, allowAnonymous);
        if (course == null) {
            System.out.println("Unable to create course!");
            return;
        }

        System.out.println("Course created:");
        System.out.println("  ID: " + course.getCourseId());
        System.out.println("  Name: " + course.getName());
        System.out.println("  Term: " + course.getTerm());
        System.out.println("  Allow anonymous posts: " + course.isAllowAnonymous());
    }

    @Override
    public String getUsage() {
        return "<name> <term> <allowAnonymous>";
    }

    @Override
    public String getDescription() {
        return "Create a new course";
    }
}
