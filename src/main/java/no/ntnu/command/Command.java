package no.ntnu.command;

public interface Command {

    default String getUsage() {
        return null;
    }

    String getDescription();

    void execute(String label, String[] args);
}
