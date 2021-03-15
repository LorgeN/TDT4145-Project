package no.ntnu.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class CommandLineRunner {

    private static final Pattern SPACE_PATTERN = Pattern.compile("\\s+");

    private final Map<String, Command> commandMap;

    public CommandLineRunner() {
        this.commandMap = new HashMap<>();
    }

    public void registerCommand(String label, Command command) {
        if (label == null) {
            throw new IllegalArgumentException("Label can not be null!");
        }

        if (command == null) {
            throw new IllegalArgumentException("Command can not be null!");
        }

        this.commandMap.put(label.toLowerCase(), command);
    }

    public Command getCommand(String label) {
        if (label == null) {
            throw new IllegalArgumentException("Label can not be null!");
        }

        return this.commandMap.get(label.toLowerCase());
    }

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

    public void startCommandLoop() {
        Scanner scanner = new Scanner(System.in);

        //noinspection InfiniteLoopStatement
        while (true) {
            String line = scanner.nextLine();
            this.runCommand(line);
        }
    }
}
