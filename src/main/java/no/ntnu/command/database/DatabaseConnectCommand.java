package no.ntnu.command.database;

import no.ntnu.App;
import no.ntnu.command.Command;
import no.ntnu.mysql.ConnectionManager;

/**
 * Sets up a connection to a database
 */
public class DatabaseConnectCommand implements Command {

    private final App app;

    public DatabaseConnectCommand(App app) {
        this.app = app;
    }

    @Override
    public String getUsage() {
        return "<url> <username> <password>";
    }

    @Override
    public String getDescription() {
        return "Sets up the credentials for database connections";
    }

    @Override
    public void execute(String label, String[] args) {
        if (args.length < 3) {
            System.out.println("Please enter url, username and password!");
            return;
        }

        String url = args[0];
        String username = args[1];
        String password = args[2];

        this.app.setConnectionManager(new ConnectionManager(url, username, password));
        this.app.getConnectionManager().testConnection();
    }
}
