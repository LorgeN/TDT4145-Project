package no.ntnu.statistics.command;

import no.ntnu.App;
import no.ntnu.auth.command.ProtectedCommand;
import no.ntnu.statistics.StatisticsObjectManager;

public class StatisticCommand extends ProtectedCommand {

    private final StatisticsObjectManager statisticsObjectManager;

    public StatisticCommand(App app) {
        super(app);
        this.statisticsObjectManager = app.getStatisticsController();
    }

    @Override
    protected void protectedExecute(String label, String[] args) {
        this.statisticsObjectManager.printStatistics();
    }

    @Override
    public String getDescription() {
        return "Prints out usefull statistics according to usecase 5";
    }
}
