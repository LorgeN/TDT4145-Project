package no.ntnu;

import no.ntnu.command.CommandLineRunner;
import no.ntnu.command.defaults.ExitCommand;
import no.ntnu.command.defaults.PingCommand;

public class App {
    public static void main(String[] args) {
        CommandLineRunner runner = new CommandLineRunner();

        runner.registerCommand("ping", new PingCommand());
        runner.registerCommand("exit", new ExitCommand());

        runner.startCommandLoop();
    }
}
