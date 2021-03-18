package no.ntnu.statistics.command;

import no.ntnu.App;
import no.ntnu.auth.command.ProtectedCommand;
import no.ntnu.statistics.StatisticsController;

public class StatisticCommand extends ProtectedCommand {

    private final StatisticsController statisticsController;

    public StatisticCommand(App app) {
        super(app);
        this.statisticsController = app.getStatisticsController();
    }

    @Override
    protected void protectedExecute(String label, String[] args) {
        this.statisticsController.printStatistics();
    }

    @Override
    public String getDescription() {
        return "Prints out usefull statistics according to usecase 5";
    }
}
