package se.bimsolution.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresRepository implements Repository {
    private Connection connection;

    public PostgresRepository(String url, String username, String password) throws RuntimeException, SQLException {
        connection = DriverManager.getConnection(url, username, password);
    }

    /**
     * This method creates a new revision instance and inserts it into the database.
     *
     * @return a new revision with Id corresponding to database Id.
     * @throws SQLException
     */
    @Override
    public Revision newRevision() throws SQLException {

        //Insert revision to database.
        Revision revision = new Revision();

        //Initialize id.
        int id = 0;
        String sqlString = "INSERT INTO revision " +
                "       (Success) " +
                "         VALUES (?)";

        PreparedStatement statement = connection.prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);
        statement.setBoolean(1, false);
        statement.executeUpdate();

        //Get the ID of the inserted Run.
        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()) {
            id = rs.getInt(1);
        }
        revision.setId(id);
        return revision;
    }

    /**
     * This method creates a new revision instance and inserts it into the database.
     *
     * @param revisionId    The revision Id that corresponds to the log.
     * @throws SQLException
     */

    @Override
    public Log createLog(int revisionId, String logMessage, int errorId) throws SQLException {
        //Initialize id.
        int id = 0;

        //Update revision in DB.
        String sqlString = "INSERT INTO log " +
                "       (revision_id, error_id, log_message) " +
                "         VALUES (?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, revisionId);
        statement.setInt(2, errorId);
        statement.setString(3, logMessage);
        statement.executeUpdate();

        //Get the ID of the inserted Run
        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()) {
            id = rs.getInt(1);
        }
        return new Log(id, logMessage, errorId, revisionId);
    }

    @Override
    public void createStats(Count count) {
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
