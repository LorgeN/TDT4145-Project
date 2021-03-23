package no.ntnu.command.course;

import no.ntnu.App;
import no.ntnu.entity.User;
import no.ntnu.command.auth.ProtectedCommand;
import no.ntnu.entity.Course;
import no.ntnu.manager.CourseObjectManager;

import java.util.Map;

public class ViewCoursesCommand extends ProtectedCommand {

    public ViewCoursesCommand(App app) {
        super(app);
    }

    @Override
    protected void protectedExecute(String label, String[] args) {
        User user = this.getApp().getUserManager().getCurrentUser();
        CourseObjectManager manager = this.getApp().getCourseObjectManager();
        Map<Course, Boolean> courses = manager.getCourses(user.getEmail());

        System.out.println("Your courses:");

        courses.forEach((course, instructor) -> {
            System.out.println(" - " + course.getName() + " (" + course.getCourseId() +
                    ") @ " + course.getTerm() + (instructor ? " (Instructor)" : ""));
        });
    }

    @Override
    public String getDescription() {
        return "View courses you are participating in";
    }
}
