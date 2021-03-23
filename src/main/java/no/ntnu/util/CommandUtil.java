package no.ntnu.util;

import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

/**
 * Class for abstract command utilities
 */
public class CommandUtil {

    /**
     * Accepts a single input line from the command line
     *
     * @param prompt The prompt
     * @return The entered value
     */
    public static String acceptInput(String prompt) {
        System.out.println(prompt);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    /**
     * Creates a dialog for selecting one out of a list of provided options, e. g.
     * for selecting a course. Uses {@code #toString()} for printing options.
     *
     * @param options The options that can be selected
     * @param <T>     The type of the option objects
     * @return The selected object
     */
    public static <T> T selectOptions(List<T> options) {
        return selectOptions(options, T::toString);
    }

    /**
     * Creates a dialog for selecting one out of a list of provided options, e. g.
     * for selecting a course
     *
     * @param options   The options that can be selected
     * @param formatter A format function, for pretty printing the option objects
     * @param <T>       The type of the option objects
     * @return The selected object
     */
    public static <T> T selectOptions(List<T> options, Function<T, String> formatter) {
        if (options.size() == 0) {
            return null;
        }

        if (options.size() == 1) {
            return options.get(0);
        }

        Scanner scanner = new Scanner(System.in);

        System.out.println("Please select an option:");

        for (int i = 0; i < options.size(); i++) {
            T option = options.get(i);
            System.out.println("  - " + (i + 1) + ": " + formatter.apply(option));
        }

        String errorOutOfBounds = "Please enter a message between 1 and " + options.size();
        System.out.println(errorOutOfBounds);

        int result = -1;
        while (result == -1) {
            String inRaw = scanner.nextLine();

            try {
                result = Integer.parseInt(inRaw);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number \"" + inRaw + "\"!");
                System.out.println(errorOutOfBounds);
                continue;
            }

            if (result < 1 || result > options.size()) {
                result = -1;
                System.out.println(errorOutOfBounds);
            }
        }

        return options.get(result - 1);
    }
}
