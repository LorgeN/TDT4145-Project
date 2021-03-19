package no.ntnu.folder.command;

import no.ntnu.command.Command;
import no.ntnu.folder.FolderController;

import java.sql.SQLException;

public class CreateFolderCommand implements Command {
    private final FolderController folderController;

    public CreateFolderCommand(FolderController folderController) {
        this.folderController = folderController;
    }

    @Override
    public String getUsage() {
        return "<name> <courseId> [parentFolderId]";
    }

    @Override
    public String getDescription() {
        return "Creates a folder with the given values. If no parentFolderId is provided, this is a top level folder";
    }

    private Integer acceptInt(String label, String arg) {
        Integer argAsInt = null;
        try {
            argAsInt = Integer.parseInt(arg);
        } catch (NumberFormatException e){
            System.out.printf("Invalid %s provided.", label);
        }

        return argAsInt;
    }

    @Override
    public void execute(String label, String[] args) {
        if (args.length < 2){
            System.out.println("Please enter at least a name and courseId \n");
            return;
        }

        String name = args[0];
        Integer courseId = acceptInt("courseId", args[1]);
        if (courseId == null) {
            return;
        }

        Integer parentFolderId = null;
        if (args.length == 3) {
            parentFolderId = acceptInt("parentFolderId", args[2]);
        }

        try {
            this.folderController.createFolder(name, courseId, parentFolderId);
        } catch (SQLException e){
            System.out.println("Could not create the folder!");
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("Please provide all values!");
        }
    }
}
