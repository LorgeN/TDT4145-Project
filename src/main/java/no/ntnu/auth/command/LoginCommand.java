package no.ntnu.auth.command;

import no.ntnu.App;
import no.ntnu.auth.AuthController;
import no.ntnu.auth.User;
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

        AuthController authController = this.app.getAuthController();
        authController.loginUser(args[0], args[1]);

        if (authController.loginUser(args[0], args[1])) {
            User user = authController.getCurrentUser();
            System.out.println("Succesfully logged in user: " + user);

            System.out.println("Registering active today...");
            this.app.getStatisticsController().registerLogin(user.getEmail());
        }
    }
}
