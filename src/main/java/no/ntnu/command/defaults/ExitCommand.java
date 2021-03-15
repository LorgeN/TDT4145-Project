package no.ntnu.command.defaults;

import no.ntnu.command.Command;

public class ExitCommand implements Command {

    @Override
    public void execute(String label, String[] args) {
        System.out.println("Good bye!");
        System.exit(0);
    }
}
