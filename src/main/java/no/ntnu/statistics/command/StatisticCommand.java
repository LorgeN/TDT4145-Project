package no.ntnu.statistics.command;

import no.ntnu.auth.AuthController;
import no.ntnu.auth.command.ProtectedCommand;
import no.ntnu.statistics.StatisticsController;

public class StatisticCommand extends ProtectedCommand {

    private StatisticsController statisticsController;

    public StatisticCommand(AuthController authController, StatisticsController statisticsController) {
        super(authController);
        this.statisticsController = statisticsController;
    }

    @Override
    protected void protectedExecute(String label, String[] args) {
        this.statisticsController.printStatistics();
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public String getDescription() {
        return "Prints out usefull statistics according to usecase 5";
    }
}
