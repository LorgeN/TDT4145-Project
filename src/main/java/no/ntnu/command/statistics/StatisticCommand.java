package no.ntnu.command.statistics;

import no.ntnu.App;
import no.ntnu.command.auth.ProtectedCommand;
import no.ntnu.manager.StatisticsObjectManager;
import no.ntnu.entity.Course;

public class StatisticCommand extends ProtectedCommand {

    private final StatisticsObjectManager statisticsObjectManager;

    public StatisticCommand(App app) {
        super(app);
        this.statisticsObjectManager = app.getStatisticsController();
    }

    @Override
    protected void protectedExecute(String label, String[] args) {
        Course course = this.getApp().getCourseObjectManager().getSelectedCourse();
        if (course == null) {
            System.out.println("No course selected!");
            return;
        }

        this.statisticsObjectManager.printStatistics(course.getCourseId());
    }

    @Override
    public String getDescription() {
        return "Prints out usefull statistics according to usecase 5";
    }
}
