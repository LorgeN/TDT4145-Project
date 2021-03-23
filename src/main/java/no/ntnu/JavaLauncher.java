package no.ntnu;

import no.ntnu.mysql.ConnectionManager;

/**
 * Launcher class
 */
public class JavaLauncher {

    public static void main(String[] args) {
        App app = new App();

        String url, username, password;

        if (args.length >= 3) {
            System.out.println("Accepting database configuration from command line...");
            url = args[0];
            username = args[1];
            password = args[2];
        } else {
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
        }

        System.out.println("Could not find database config in environment! Please enter one using the dbconnect command.");
        app.startRunner();
    }
}
