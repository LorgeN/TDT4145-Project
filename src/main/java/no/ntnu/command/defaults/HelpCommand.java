package no.ntnu.command.defaults;

import no.ntnu.command.Command;
import no.ntnu.command.CommandLineRunner;

/**
 * Lists all registered commands
 */
public class HelpCommand implements Command {

    private final CommandLineRunner runner;

    /**
     * @param runner The command line runner instance
     */
    public HelpCommand(CommandLineRunner runner) {
        this.runner = runner;
    }

    @Override
    public String getDescription() {
        return "Shows a list of all commands";
    }

    @Override
    public void execute(String label, String[] args) {
        System.out.println("All available commands:");

        this.runner.getCommands().entrySet().stream()
                .map(entry -> this.makeHelpMessage(entry.getKey(), entry.getValue()))
                .forEach(System.out::println);
    }

    private String makeHelpMessage(String label, Command command) {
        if (command.getUsage() == null) {
            return label + " - " + command.getDescription();
        }

        return label + " " + command.getUsage() + " - " + command.getDescription();
    }
}
