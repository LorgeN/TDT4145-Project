package no.ntnu.command.auth;

import no.ntnu.App;
import no.ntnu.command.Command;

/**
 * Command for listing all users
 */
public class AllUsersCommand implements Command {

    private final App app;

    public AllUsersCommand(App app) {
        this.app = app;
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public String getDescription() {
        return "Prints out a list of all users";
    }

    @Override
    public void execute(String label, String[] args) {
        this.app.getUserManager().getAllUsers().forEach(System.out::println);
    }
}
