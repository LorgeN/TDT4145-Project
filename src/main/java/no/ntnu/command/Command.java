package no.ntnu.command;

/**
 * A command that can be called from the command line
 */
public interface Command {

    /**
     * Provides a description of the arguments this command takes. For example,
     * if the command takes in two arguments {@code amount} and {@code delay},
     * where {@code delay} is optional, this should return {@code <amount> [delay]}.
     *
     * @return A description of the arguments this command takes
     */
    default String getUsage() {
        return null;
    }

    /**
     * @return A description of this command's functionality
     */
    String getDescription();

    /**
     * Calls this command
     *
     * @param label The label this command was called with
     * @param args  The arguments used when calling this command, as strings
     */
    void execute(String label, String[] args);
}
