package no.ntnu.command.auth;

import no.ntnu.App;

public class CurrentUserCommand extends ProtectedCommand {

    public CurrentUserCommand(App app) {
        super(app);
    }

    @Override
    protected void protectedExecute(String label, String[] args) {
        System.out.println(this.getApp().getUserManager().getCurrentUser());

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
