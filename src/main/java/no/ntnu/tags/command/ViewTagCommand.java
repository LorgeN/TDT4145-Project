package no.ntnu.tags.command;

import no.ntnu.App;
import no.ntnu.command.Command;
import no.ntnu.tags.Tag;
import no.ntnu.tags.TagObjectManager;

import java.util.List;

public class ViewTagCommand implements Command {

    private final App app;

    public ViewTagCommand(App app) {
        this.app = app;
    }

    @Override
    public String getUsage() {
        return "[course]";
    }

    @Override
    public String getDescription() {
        return "View available tags";
    }

    @Override
    public void execute(String label, String[] args) {
        TagObjectManager manager = this.app.getTagObjectManager();

        List<Tag> tags;
        if (args.length == 0) {
            tags = manager.getTags();
        } else {
            try {
                int courseId = Integer.parseInt(args[0]);
                tags = manager.getCourseTags(courseId);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a course ID (int)!");
                return;
            }
        }

        System.out.println("Found " + tags.size() + " tag(s):");
        for (Tag tag : tags) {
            System.out.println(" - " + tag.getCourseId() + "/" + tag.getName());
        }
    }
}
