package no.ntnu.auth.command;

import no.ntnu.auth.AuthController;

public class CurrentUserCommand extends ProtectedCommand {

    public CurrentUserCommand(AuthController authController) {
        super(authController);
    }

    @Override
    protected void protectedExecute(String label, String[] args) {
        System.out.println(this.authController.getCurrentUser());

    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public String getDescription() {
        return "Prints email and name for the currently logged in user. Prints an error message if no user is logged in";
    }

}
