package no.ntnu.auth.command;

import no.ntnu.App;
import no.ntnu.auth.UserObjectManager;
import no.ntnu.command.Command;

/**
 * Command for logging in a user
 */
public class LoginCommand implements Command {

    private final App app;

    public LoginCommand(App app) {
        this.app = app;
    }


    @Override
    public String getUsage() {
        return "<email> <password>";
    }

    @Override
    public String getDescription() {
        return "Tries to log in with the given credentials";
    }

    @Override
    public void execute(String label, String[] args) {
        if (args.length != 2) {
            System.out.println("Please provide email and password");
            return;
        }

        UserObjectManager userObjectManager = this.app.getAuthController();
        userObjectManager.loginUser(args[0], args[1]);

        if (userObjectManager.loginUser(args[0], args[1])) {
            System.out.println("Succesfully logged in user: " + userObjectManager.getCurrentUser());
        }

    }
}
