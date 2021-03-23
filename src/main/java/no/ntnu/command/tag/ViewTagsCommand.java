package no.ntnu.command.tag;

import no.ntnu.App;
import no.ntnu.command.Command;
import no.ntnu.entity.Course;
import no.ntnu.entity.Tag;
import no.ntnu.manager.TagObjectManager;

import java.util.List;
import java.util.Map;

public class ViewTagsCommand implements Command {

    private final App app;

    public ViewTagsCommand(App app) {
        this.app = app;
    }

    @Override
    public String getDescription() {
        return "View available tags";
    }

    @Override
    public void execute(String label, String[] args) {
        TagObjectManager manager = this.app.getTagObjectManager();

        Map<Course, List<Tag>> tags = manager.getTags();

        System.out.println("Available tags:");

        tags.forEach((course, courseTags) -> {
            System.out.println(course.getName() + ":");
            courseTags.forEach(tag -> System.out.println(" - " + tag.getName()));
        });
    }
}
