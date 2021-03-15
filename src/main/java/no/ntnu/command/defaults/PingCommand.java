package no.ntnu.command.defaults;

import no.ntnu.command.Command;

/**
 * Simple command that just prints "Pong!" a number of times
 */
public class PingCommand implements Command {

    @Override
    public String getUsage() {
        return "[amount]";
    }

    @Override
    public String getDescription() {
        return "Prints \"Pong!\" some amount of times";
    }

    @Override
    public void execute(String label, String[] args) {
        int amount = 1;
        if (args.length > 0) {
            try {
                amount = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount \"" + args[0] + "\"!");
                return;
            }
        }

        for (int i = 0; i < amount; i++) {
            System.out.println("Pong!");
        }
    }
}
