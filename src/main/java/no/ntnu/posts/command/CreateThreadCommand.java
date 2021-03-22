package no.ntnu.posts.command;

import no.ntnu.App;
import no.ntnu.auth.command.ProtectedCommand;
import no.ntnu.course.Course;
import no.ntnu.folder.Folder;
import no.ntnu.folder.FolderController;
import no.ntnu.posts.PostObjectManager;
import no.ntnu.posts.Thread;
import no.ntnu.tags.Tag;
import no.ntnu.tags.TagObjectManager;
import no.ntnu.util.CommandUtil;

import java.util.List;

public class CreateThreadCommand extends ProtectedCommand {

    public CreateThreadCommand(App app) {
        super(app);
    }

    @Override
    public String getUsage() {
        return "<folder> <tag> [anonymous]";
    }

    @Override
    public String getDescription() {
        return "Create a new thread";
    }

    @Override
    protected void protectedExecute(String label, String[] args) {
        Course course = this.getApp().getCourseObjectManager().getSelectedCourse();
        if (course == null) {
            System.out.println("Please select a course!");
            return;
        }

        if (args.length < 2) {
            System.out.println("Please enter folder and tag!");
            return;
        }

        boolean anonymous = false;
        if (args.length > 2) {
            anonymous = Boolean.parseBoolean(args[2]);
        }

        FolderController folderController = this.getApp().getFolderController();
        List<Folder> folders = folderController.getFoldersByName(course.getCourseId(), args[0]);
        Folder folder = CommandUtil.selectOptions(folders);
        if (folder == null) {
            System.out.println("Could not find folder \"" + args[0] + "\"!");
            return;
        }

        TagObjectManager tagManager = this.getApp().getTagObjectManager();
        List<Tag> tags = tagManager.getCourseTags(course.getCourseId(), args[1]);
        Tag tag = CommandUtil.selectOptions(tags);
        if (tag == null) {
            System.out.println("Could not find tag \"" + args[1] + "\"!");
            return;
        }

        String title = CommandUtil.acceptInput("Please enter post title:");
        String text = CommandUtil.acceptInput("Please enter post text:");

        PostObjectManager manager = this.getApp().getPostObjectManager();
        Thread thread = manager.makeThread(title, course.getCourseId(), folder.getFolderId(), tag.getName(), anonymous, text);
        System.out.println("Thread created: " + thread);
    }
}
