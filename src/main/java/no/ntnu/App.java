package no.ntnu;

import no.ntnu.command.CommandLineRunner;

public class App {
    public static void main(String[] args) {
        CommandLineRunner runner = new CommandLineRunner();

        // TODO: Register system commands

        runner.startCommandLoop();
    }
}
