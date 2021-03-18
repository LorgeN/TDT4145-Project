package no.ntnu.auth.command;

import no.ntnu.auth.AuthController;
import no.ntnu.command.Command;

import java.sql.SQLException;

/**
 * Command for creating a new user
 */
public class CreateUserCommand implements Command {

    private final AuthController authController;
    public CreateUserCommand(AuthController authController) {
        this.authController = authController;
    }

    @Override
    public String getUsage() {
        return "<email> <name> <password>";
    }

    @Override
    public String getDescription() {
        return "Creates a user with the given values";
    }

    @Override
    public void execute(String label, String[] args) {
        if (args.length != 3) {
            System.out.println("Please enter email, name and password!");
            return;
        }

        String email = args[0];
        String name = args[1];
        String password = args[2];

        try {
            this.authController.createUser(email, name, password);
            System.out.println("User created!");
        } catch (SQLException e) {
            System.out.println("Could not create the user!");
        }


    }
}
