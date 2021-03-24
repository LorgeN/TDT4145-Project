package no.ntnu.command.folder;

import no.ntnu.App;
import no.ntnu.command.auth.ProtectedCommand;
import no.ntnu.entity.Course;
import no.ntnu.entity.Folder;
import no.ntnu.manager.FolderObjectManager;
import no.ntnu.util.CommandUtil;

import java.util.List;

/**
 * Command to create a folder
 */
public class CreateFolderCommand extends ProtectedCommand {
    private final FolderObjectManager folderObjectManager;

    public CreateFolderCommand(App app) {
        super(app);
        this.folderObjectManager = app.getFolderManager();
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
            List<Folder> folders = folderObjectManager.getFoldersByName(course.getCourseId(), parentFolderName);
            parentFolderId = CommandUtil.selectOptions(folders).getFolderId();

            if (folders == null) {
                System.out.println("Could not find any potential parent folders with that name");
                return;
            }
        }

        try {
            this.folderObjectManager.createFolder(name, courseId, parentFolderId);
        }  catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("Please provide all values!");
        }
    }
}
