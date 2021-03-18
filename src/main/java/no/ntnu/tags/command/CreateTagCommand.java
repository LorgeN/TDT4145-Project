package no.ntnu.tags.command;

import no.ntnu.App;
import no.ntnu.auth.User;
import no.ntnu.auth.command.ProtectedCommand;
import no.ntnu.course.Course;
import no.ntnu.course.CourseObjectManager;
import no.ntnu.tags.Tag;
import no.ntnu.tags.TagObjectManager;
import no.ntnu.util.CommandUtil;

import java.util.List;

public class CreateTagCommand extends ProtectedCommand {

    public CreateTagCommand(App app) {
        super(app);
    }

    @Override
    public String getUsage() {
        return "<course> <name>";
    }

    @Override
    public String getDescription() {
        return "Create a new tag";
    }

    @Override
    protected void protectedExecute(String label, String[] args) {
        if (args.length < 2) {
            System.out.println("Please specify a course and tag name");
            return;
        }

        User selfUser = this.getApp().getAuthController().getCurrentUser();
        CourseObjectManager courseManager = this.getApp().getCourseObjectManager();

        String courseName = args[0];
        List<Course> courses = courseManager.getInstructorCourses(selfUser.getEmail(), courseName);
        Course course = CommandUtil.selectOptions(courses);
        if (course == null) {
            System.out.println("Could not find any course \"" + courseName + "\" that you are an instructor in!");
            return;
        }

        TagObjectManager tagManager = this.getApp().getTagObjectManager();
        Tag tag = tagManager.createTag(course.getCourseId(), args[1]);
        if (tag == null) {
            System.out.println("Unable to create tag!");
            return;
        }

        System.out.println("Created new tag \"" + tag.getName() + "\" in course " + course.getName() + "!");
    }
}
