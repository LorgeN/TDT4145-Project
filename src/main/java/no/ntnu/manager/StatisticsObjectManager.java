package no.ntnu.manager;

import no.ntnu.App;
import no.ntnu.mysql.ActiveDomainObjectManager;
import no.ntnu.mysql.ConnectionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class StatisticsObjectManager extends ActiveDomainObjectManager {

    // Use insert ignore so we dont have to actually check if this exists, which saves 1 query
    private static final String INSERT_READ_STATEMENT = "INSERT IGNORE INTO postread(User, PostId) VALUES (?, ?);";
    private static final String INSERT_LOGIN_STATEMENT = "INSERT IGNORE INTO activedays(User, Date) VALUES (?, NOW());";

    public StatisticsObjectManager(App app) {
        super(app);
    }

    public void registerLogin(String user) {
        try (Connection connection = this.connectionManager.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(INSERT_LOGIN_STATEMENT);
            statement.setString(1, user);

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void readPost(String user, List<Integer> posts) {
        try (Connection connection = this.connectionManager.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(INSERT_READ_STATEMENT);
            statement.setString(1, user);

            for (Integer post : posts) {
                statement.setInt(2, post);
                statement.addBatch();
            }

            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints out the statistics according to the 5th usecase
     */
    public void printStatistics(int courseId) {

        String queryString;
        try {
            queryString = this.getQuery();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try (Connection connection = this.connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(queryString)) {
            statement.setInt(1, courseId);

            ResultSet result = statement.executeQuery();

            if (result.isBeforeFirst()) {
                printRow("Name", "Email", "antall_lest", "antall_opprettet");
                while (result.next()) {
                    String name = result.getString("Name");
                    String email = result.getString("Email");
                    String antallLest = result.getString("antall_lest");
                    String antallOpprettet = result.getString("antall_opprettet");
                    printRow(name, email, antallLest, antallOpprettet);
                }
            } else {
                System.out.println("No result...");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper function for printing out a single row with padding
     *
     * @param columns list of string values for each column in the row
     */
    private void printRow(String... columns) {
        for (int i = 0; i < columns.length; i++) {
            boolean isLast = i == columns.length - 1;
            printColumn(columns[i], isLast);

        }
    }

    /**
     * Helper function for printing out a column in a row with padding
     *
     * @param columnValue the string value for the column
     * @param isLast      flag for if it is the last column. If true this will append a newline at the end
     */
    private void printColumn(String columnValue, boolean isLast) {
        String end = isLast ? "\n" : "";
        System.out.print(String.format("%-30s", columnValue) + end);
    }


    /**
     * Helper function for getting the query string from file
     *
     * @return the query string if found
     * @throws IOException if the file cannot be found
     */
    private String getQuery() throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("no/ntnu/statistics/statistic_query.sql");
        if (is != null) {
            try (InputStreamReader inputStreamReader = new InputStreamReader(is);
                 BufferedReader reader = new BufferedReader(inputStreamReader)) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } else {
            throw new IOException("Could not find the file");
        }

    }
}
