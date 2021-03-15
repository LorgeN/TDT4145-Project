package no.ntnu;

import no.ntnu.mysql.ConnectionManager;

/**
 * Launcher class
 */
public class JavaLauncher {

    public static void main(String[] args) {
        App app = new App();

        if (args.length >= 3) {
            String url = args[0];
            String username = args[1];
            String password = args[2];

            app.setConnectionManager(new ConnectionManager(url, username, password));
            app.getConnectionManager().testConnection();
        }

        app.startRunner();
    }
}
