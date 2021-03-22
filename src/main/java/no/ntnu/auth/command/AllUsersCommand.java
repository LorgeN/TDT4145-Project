package no.ntnu.auth.command;

import no.ntnu.App;
import no.ntnu.command.Command;

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
        this.app.getAuthController().getAllUsers().stream().forEach(System.out::println);
    }
}