package no.ntnu.command.auth;

import no.ntnu.App;
import no.ntnu.command.Command;
import no.ntnu.manager.UserObjectManager;

/**
 * Command for logging out
 */
public class LogoutUserCommand implements Command {
    private final App app;

    public LogoutUserCommand(App app) {
        this.app = app;
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public String getDescription() {
        return "Logs out the current user if any is logged in";
    }

    @Override
    public void execute(String label, String[] args) {
        UserObjectManager userObjectManager = this.app.getUserManager();
        userObjectManager.logoutUser();
    }
}
