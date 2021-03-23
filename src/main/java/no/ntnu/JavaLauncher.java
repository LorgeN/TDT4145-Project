package no.ntnu;

import no.ntnu.mysql.ConnectionManager;

/**
 * Launcher class
 */
public class JavaLauncher {

    public static void main(String[] args) {
        App app = new App();

        String url, username, password;

        // If the argument count is less than three it is not possible for there to be a
        // provided url, username and password.
        if (args.length >= 3) {
            System.out.println("Accepting database configuration from command line...");
            url = args[0];
            username = args[1];
            password = args[2];
        } else {
            // Check if there are environment variables present. This is supported because
            // it greatly simplifies running in a preconfigured environment, such as an IDE.
            System.out.println("Accepting database configuration from environment...");
            url = System.getenv("DB_URL");
            username = System.getenv("DB_USERNAME");
            password = System.getenv("DB_PASSWORD");
        }

        if (url != null && username != null && password != null) {
            System.out.println("Using ");
            System.out.println(" URL: " + url);
            System.out.println(" Username: " + username);
            System.out.println(" Password: " + password);

            app.setConnectionManager(new ConnectionManager(url, username, password));
        } else {
            // Ensure that the user is informed that they can also use the command to enter credentials
            System.out.println("Could not find database config in environment! Please enter one using the dbconnect command.");
        }

        app.startRunner();
    }
}
