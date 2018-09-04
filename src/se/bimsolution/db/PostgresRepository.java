package se.bimsolution.db;

import java.sql.*;
import java.util.List;

public class PostgresRepository implements Repository {
    private Connection connection;
    private Boolean inTransaction = false;
    private Run run = new Run();

    public PostgresRepository(String connstr) throws RuntimeException {
        try {
            connection = DriverManager.getConnection(connstr);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Run newRun() {
        Run run = new Run();

        run.setSuccess(true);
        return run;
    }

    @Override
    public void updateRun(Run run) {
        int runID = 0;

        try {
            //Insert Run to DB
            String runSQL = "INSERT INTO Run " +
                    "       (Success) " +
                    "         VALUES (?)";

            PreparedStatement statement = connection.prepareStatement(runSQL, Statement.RETURN_GENERATED_KEYS);
            statement.setBoolean(1, run.getSuccess());
            statement.executeUpdate();

            //Get the ID of the inserted Run
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                runID = rs.getInt(1);
            }
            run.setId(runID);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void writeCount(Count count) {
        int runID = 0;

        try {
            //Insert Run to DB
            String runSQL = "INSERT INTO Count " +
                    "       (objectCount, failCount, runID,qID) " +
                    "         VALUES (?,?,?,?)";

            PreparedStatement statement = connection.prepareStatement(runSQL, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, count.getObjectCount());
            statement.setInt(2, count.getFailCount());
            statement.setInt(3, count.getRunID());
            statement.setInt(4, count.getQID());
            statement.executeUpdate();

            //Get the ID of the inserted Run
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                runID = rs.getInt(1);
            }
            count.setID(runID);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public void writeAllFails(List<Fail> fails) {

    }


    @Override
    public void writeLog(Log log) {

    }


}
