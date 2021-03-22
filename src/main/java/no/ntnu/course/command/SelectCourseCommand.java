package no.ntnu.course.command;

import no.ntnu.App;
import no.ntnu.auth.command.ProtectedCommand;

public class SelectCourseCommand extends ProtectedCommand {
    public SelectCourseCommand(App app) {
        super(app);
    }

    @Override
    protected void protectedExecute(String label, String[] args) {
        if (args.length < 1){
            System.out.println("Please provide a course name");
            return;
        }
        String name = args[0];
        this.getApp().getCourseObjectManager().selectCourse(name);
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
