package no.ntnu.command.defaults;

import no.ntnu.command.Command;

public class ExitCommand implements Command {

    @Override
    public String getDescription() {
        return "Exits the application";
    }

    @Override
    public void execute(String label, String[] args) {
        System.out.println("Good bye!");
        System.exit(0);
    }
}
