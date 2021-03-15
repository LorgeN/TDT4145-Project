package no.ntnu.command;

import no.ntnu.command.defaults.ExitCommand;
import no.ntnu.command.defaults.HelpCommand;
import no.ntnu.command.defaults.PingCommand;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Simple command line interface runner. Abstracts out the basic logic
 * of command recognition, and calling the correct command, based on the
 * given input.
 */
public class CommandLineRunner {

    private static final Pattern SPACE_PATTERN = Pattern.compile("\\s+");

    private final Map<String, Command> commandMap;

    /**
     * Creates a new command line runner instance
     */
    public CommandLineRunner() {
        this.commandMap = new HashMap<>();

        this.registerCommand("help", new HelpCommand(this));
        this.registerCommand("ping", new PingCommand());
        this.registerCommand("exit", new ExitCommand());
    }

    /**
     * @return A mutable instance of all commands registered in this runner
     */
    public Map<String, Command> getCommands() {
        return this.commandMap;
    }

    /**
     * Register a new command with the given label
     *
     * @param label   The label of the command
     * @param command The command to register
     */
    public void registerCommand(String label, Command command) {
        if (label == null) {
            throw new IllegalArgumentException("Label can not be null!");
        }

        if (command == null) {
            throw new IllegalArgumentException("Command can not be null!");
        }

        this.commandMap.put(label.toLowerCase(), command);
    }

    /**
     * Finds the command with the given label
     *
     * @param label The label
     * @return The command if found, or {@code null}
     */
    public Command getCommand(String label) {
        if (label == null) {
            throw new IllegalArgumentException("Label can not be null!");
        }

        return this.commandMap.get(label.toLowerCase());
    }

    /**
     * Attempts to execute the given command line
     *
     * @param commandLine The command line to run
     */
    public void runCommand(String commandLine) {
        String[] args = SPACE_PATTERN.split(commandLine);
        if (args.length == 0) {
            System.out.println("Please enter a command!");
            return;
        }

        String label = args[0].toLowerCase();
        Command target = this.getCommand(label);
        if (target == null) {
            System.out.println("Could not find command \"" + label + "\"!");
            return;
        }

        try {
            String[] arguments = Arrays.copyOfRange(args, 1, args.length);
            target.execute(label, arguments);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * Starts an infinite loop, which reads from {@link System#in} and attempts
     * to execute each command call
     */
    public void startCommandLoop() {
        Scanner scanner = new Scanner(System.in);

        //noinspection InfiniteLoopStatement
        while (true) {
            String line = scanner.nextLine();
            this.runCommand(line);
        }
    }
}
