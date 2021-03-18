package no.ntnu.auth.command;

import no.ntnu.auth.AuthController;
import no.ntnu.command.Command;

/**
 * An extension of Command that can only be called when the given authController has a logged in user
 */
public abstract class ProtectedCommand implements Command {
    protected AuthController authController;

    public ProtectedCommand(AuthController authController) {
        this.authController = authController;
    }

    abstract void protectedExecute(String label, String[] args);


    /**
     * This ensures that only logged in users will be able to run the command
     *
     * @param label The label this command was called with
     * @param args  The arguments used when calling this command, as strings
     */
    @Override
    public void execute(String label, String[] args) {
        if (!this.authController.isAuthenticated()) {
            System.out.println("You need to be logged in to run this command!");
            return;
        }

        this.protectedExecute(label, args);
    }

    public void setAuthController(AuthController authController) {
        this.authController = authController;
    }
}
