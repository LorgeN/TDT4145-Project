package no.ntnu.folder.command;

import no.ntnu.App;
import no.ntnu.auth.command.ProtectedCommand;
import no.ntnu.course.Course;
import no.ntnu.folder.Folder;
import no.ntnu.folder.FolderController;
import no.ntnu.util.CommandUtil;

import java.sql.SQLException;
import java.util.List;

public class CreateFolderCommand extends ProtectedCommand {
    private final FolderController folderController;

    public CreateFolderCommand(App app) {
        super(app);
        this.folderController = app.getFolderController();
    }

    @Override
    public String getUsage() {
        return "<name> [parentFolderId]";
    }

    @Override
    public String getDescription() {
        return "Creates a folder with the given name. If no parentFolderId is provided, this is a top level folder";
    }

    @Override
    protected void protectedExecute(String label, String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter at least a name");
            return;
        }

        String name = args[0];
        Course course = this.getApp().getCourseObjectManager().getSelectedCourse();
        if (course == null) {
            System.out.println("You have to select a course before you can create a folder!");
            return;
        }
        int courseId = course.getCourseId();

        Integer parentFolderId = null;
        if (args.length == 3) {
            String parentFolderName = args[2];
            List<Folder> folders = folderController.getFoldersByName(courseId, parentFolderName);
            parentFolderId = CommandUtil.selectOptions(folders).getFolderId();

            if (folders == null) {
                System.out.println("Could not find any potential parent folders with that name");
                return;
            }
        }

        try {
            this.folderController.createFolder(name, courseId, parentFolderId);
        } catch (SQLException e) {
            System.out.println("Could not create the folder!");
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("Please provide all values!");
        }
    }
}
