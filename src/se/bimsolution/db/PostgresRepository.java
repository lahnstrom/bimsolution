package se.bimsolution.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresRepository implements Repository {
    private Connection connection;

    public PostgresRepository(String url, String username, String password) throws RuntimeException {
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Run newRun() {
        Run run = new Run();
        int runID = 0;
        try {
            //Insert Run to DB
            String runSQL = "INSERT INTO runs " +
                    "       (Success) " +
                    "         VALUES (?)";

            PreparedStatement statement = connection.prepareStatement(runSQL, Statement.RETURN_GENERATED_KEYS);
            statement.setBoolean(1, false);
            statement.executeUpdate();

            //Get the ID of the inserted Run
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                runID = rs.getInt(1);
            }
            run.setId(runID);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return run;
    }

    @Override
    public void updateRun(Run run) {
        int runID = 0;

        try {
            //Update Run to DB
            String runSQL = "UPDATE runs " +
                    "                SET Success = ?" +
                    "                WHERE ID = ?";
            PreparedStatement statement = connection.prepareStatement(runSQL, Statement.RETURN_GENERATED_KEYS);
            statement.setBoolean(1, run.getSuccess());
            statement.setInt(2, run.getId());
            statement.executeUpdate();

            //Get the ID of the inserted Run
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                runID = rs.getInt(1);
            }
            run.setId(runID);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeCount(Count count) {
        int countID = 0;

        try {
            //Insert Run to DB
            String countSQL = "INSERT INTO Counts " +
                    "       (objectcount, failcount, run_id,q_id) " +
                    "         VALUES (?,?,?,?)";

            PreparedStatement statement = connection.prepareStatement(countSQL, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, count.getObjectCount());
            statement.setInt(2, count.getFailCount());
            statement.setInt(3, count.getRunID());
            statement.setInt(4, count.getQID());
            statement.executeUpdate();

            //Get the ID of the inserted Run
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                countID = rs.getInt(1);
            }
            count.setID(countID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeAllFails(List<Fail> fails) {

        try {
            String runSQL = "INSERT INTO Fails " +
                    "       (o_id, run_id, q_id) " +
                    "         VALUES (?,?,?)";
            PreparedStatement statement = connection.prepareStatement(runSQL);

            for (Fail fail : fails) {
                statement.setLong(1, fail.getObjectID());
                statement.setInt(2, fail.getRunID());
                statement.setInt(3, fail.getQID());
                statement.addBatch();
            }
            statement.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeLog(Log log) {

        int logID = 0;

        try {
            String countSQL = "INSERT INTO Log " +
                    "       (logmessage, run_id, q_id) " +
                    "         VALUES (?,?,?)";

            PreparedStatement statement = connection.prepareStatement(countSQL, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, log.getLogMessage());
            statement.setInt(2, log.getRunID());
            statement.setInt(3, log.qID);
            statement.executeUpdate();

            //Get the ID of the inserted Run
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                logID = rs.getInt(1);
            }
            log.setID(logID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Query> getAllQueries() {
        List<Query> queries = new ArrayList<>();
        String statement = "SELECT * FROM queries";

        try (PreparedStatement sth = connection.prepareStatement(statement)) {

            ResultSet resultSet = sth.executeQuery();

            while (resultSet.next()) {
                queries.add(createQuery(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return queries;
    }

    public Query createQuery(ResultSet resultSet) throws SQLException {
        Query query = new Query(resultSet.getInt(1), resultSet.getString(2));
        return query;
    }
}
