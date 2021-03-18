package no.ntnu.course.command;

import no.ntnu.App;
import no.ntnu.auth.AuthController;
import no.ntnu.auth.User;
import no.ntnu.auth.command.ProtectedCommand;
import no.ntnu.course.Course;
import no.ntnu.course.CourseObjectManager;
import no.ntnu.course.CourseParticipant;
import no.ntnu.util.CommandUtil;

import java.util.List;

public class InviteUserCommand extends ProtectedCommand {

    public InviteUserCommand(App app) {
        super(app);
    }

    @Override
    protected void protectedExecute(String label, String[] args) {
        if (args.length < 3) {
            System.out.println("Please enter a user and a course to add them to, and specify if they" +
                    " should be an instructor!");
            return;
        }

        AuthController authController = this.getApp().getAuthController();

        String userEmail = args[0];
        User user = authController.getUserByEmail(userEmail);
        if (user == null) {
            System.out.println("No user \"" + userEmail + "\" found!");
            return;
        }

        User selfUser = authController.getCurrentUser();
        if (selfUser.equals(user)) {
            System.out.println("You can not invite yourself!");
            return;
        }

        CourseObjectManager manager = this.getApp().getCourseObjectManager();

        String courseName = args[1];
        List<Course> courses = manager.getInstructorCourses(selfUser.getEmail(), courseName);
        Course course = CommandUtil.selectOptions(courses);
        if (course == null) {
            System.out.println("Could not find any course \"" + courseName + "\" that you are an instructor in!");
            return;
        }

        boolean instructor = Boolean.parseBoolean(args[2]);
        CourseParticipant participant = manager.addParticipant(user.getEmail(), course.getCourseId(), instructor);
        System.out.println("Added participant to course " + course + ":");
        System.out.println("  Email: " + participant.getUser());
        System.out.println("  Instructor: " + participant.isInstructor());
    }

    @Override
    public String getUsage() {
        return "<user> <course> <instructor>";
    }

    @Override
    public String getDescription() {
        return "Invite a user to a course";
    }
}
