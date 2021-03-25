package no.ntnu.command.course;

import no.ntnu.App;
import no.ntnu.manager.UserObjectManager;
import no.ntnu.entity.User;
import no.ntnu.command.auth.ProtectedCommand;
import no.ntnu.entity.Course;
import no.ntnu.manager.CourseObjectManager;
import no.ntnu.entity.CourseParticipant;
import no.ntnu.util.CommandUtil;

import java.util.List;

/**
 * Command to invite a user
 */
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

        UserObjectManager userObjectManager = this.getApp().getUserManager();

        String userEmail = args[0];
        User user = userObjectManager.getUserByEmail(userEmail);
        if (user == null) {
            System.out.println("No user \"" + userEmail + "\" found!");
            return;
        }

        User selfUser = userObjectManager.getCurrentUser();
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
        if (participant == null) {
            System.out.println("Unable to add participant to course!");
            return;
        }

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
