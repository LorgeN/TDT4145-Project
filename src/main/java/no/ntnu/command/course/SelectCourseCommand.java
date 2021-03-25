package no.ntnu.command.course;

import no.ntnu.App;
import no.ntnu.command.auth.ProtectedCommand;
import no.ntnu.entity.Course;
import no.ntnu.manager.CourseObjectManager;
import no.ntnu.util.CommandUtil;

import java.util.List;

/**
 * Command to select a course
 */
public class SelectCourseCommand extends ProtectedCommand {
    public SelectCourseCommand(App app) {
        super(app);
    }

    @Override
    protected void protectedExecute(String label, String[] args) {
        if (args.length < 1) {
            System.out.println("Please provide a course name");
            return;
        }

        String name = args[0];
        CourseObjectManager manager = this.getApp().getCourseObjectManager();
        String email = this.getApp().getUserManager().getCurrentUser().getEmail();
        List<Course> courses = manager.getCoursesByName(email, name);
        Course course = CommandUtil.selectOptions(courses);
        if (course == null) {
            System.out.println("No course by that name found!");
            return;
        }

        manager.selectCourse(course);
        System.out.println(course + " selected!");
    }

    @Override
    public String getUsage() {
        return "<name>";
    }

    @Override
    public String getDescription() {
        return "selects a course to be used in further commands";
    }
}
