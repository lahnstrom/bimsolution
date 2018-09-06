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
    public Revision writeRevision(int projectId, String model) throws SQLException {

        //Insert revision to database.
        Revision revision = new Revision(projectId, model);
        String sqlString = "INSERT INTO revision " +
                "       (model, project_id) " +
                "         VALUES (?,?)";

        PreparedStatement statement = connection.prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, model);
        statement.setInt(2, projectId);

        statement.executeUpdate();

        //Get the ID of the inserted Run.
        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()) {
            int id = rs.getInt(1);
            Timestamp timestamp = rs.getTimestamp(2);
            revision.setId(id);
            revision.setDate(timestamp);

        }

        return revision;
    }

    /**
     * This method creates a new stats instance and inserts it into the database.
     *
     * @param stats
     * @throws SQLException
     */

    @Override
    public Stats writeStats(Stats stats) throws SQLException {

        //Insert Run to DB.
        String countSQL = "INSERT INTO stats " +
                "       (object_count, fail_count, revision_id, error_id) " +
                "         VALUES (?,?,?,?)";

        PreparedStatement statement = connection.prepareStatement(countSQL, Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, stats.getObjectCount());
        statement.setInt(2, stats.getFailCount());
        statement.setInt(3, stats.getRevisionId());
        statement.setInt(4, stats.getErrorId());
        statement.executeUpdate();

        //Get the ID of the inserted Run.
        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()) {
            stats.setId(rs.getInt(1));
        }
        return stats;
    }

    /**
     * This method writes all fails from a list of fails to the database.
     *
     * @param fails List of fails.
     * @throws SQLException
     */

    @Override
    public void writeAllFails(List<Fail> fails) throws SQLException {
        String sqlString = "INSERT INTO Fail " +
                "       (object_id, revision_id, error_id, ifc_type, ifc_site, ifc_building, ifc_storey, " +
                "p_set_benamning, p_set_beteckning, p_set_typeid, p_set_ifyllt_bsab, p_set_giltiga_bsab," +
                "p_set_parameter_som_saknas) " +
                "         VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sqlString);

        for (Fail fail : fails) {
            statement.setLong(1, fail.getObjectId());
            statement.setInt(2, fail.getRevisionId());
            statement.setInt(3, fail.getErrorId());
            statement.setString(4, fail.getIfcType());
            statement.setString(5, fail.getIfcSite());
            statement.setString(6, fail.getIfcBuilding());
            statement.setString(7, fail.getIfcStorey());
            statement.setString(8, fail.getpSetBenamning());
            statement.setString(9, fail.getpSetBetackning());
            statement.setString(10, fail.getpSetTypeId());
            statement.setString(11, fail.getpSetIfylltBsab());
            statement.setString(12, fail.getpSetGiltigBsab());
            statement.setString(13, fail.getpSetParameterSomSaknas());
            statement.addBatch();
        }
        statement.executeBatch();
    }

    /**
     * This method insert revision Id  to corresponding log row into the database.
     *
     * @param log           Log instance to be updated in the database.
     * @param revisionId    Revision Id of corresponding revision.
     * @throws SQLException
     */

    @Override
    public void writeRevisionIdToLog(Log log, int revisionId) throws SQLException {

        //Update revision in DB.
        String sqlString = "UPDATE  log " +
                "       SET revision_id=? " +
                "         WHERE id=?";

        PreparedStatement statement = connection.prepareStatement(sqlString);
        statement.setInt(1, revisionId);
        statement.setInt(2,log.getId());
        statement.executeUpdate();
    }

    /**
     * This method insert error Id to corresponding log row into the database.
     *
     * @param log           Log instance to be updated in the database.
     * @param errorId       Error Id of corresponding revision.
     * @throws SQLException
     */

    @Override
    public void writeErrorIdToLog(Log log, int errorId) throws SQLException {

        //Update revision in DB.
        String sqlString = "UPDATE  log " +
                "       SET error_id=? " +
                "         WHERE id=?";

        PreparedStatement statement = connection.prepareStatement(sqlString);
        statement.setInt(1, errorId);
        statement.setInt(2,log.getId());
        statement.executeUpdate();
    }

    /**
     * This method insert log message to corresponding log row into the database.
     *
     * @param log           Log instance to be updated in the database.
     * @param logMessage    Log message of corresponding revision.
     * @throws SQLException
     */

    @Override
    public void writeLogMessageIdToLog(Log log, String logMessage) throws SQLException {

        //Update revision in DB.
        String sqlString = "UPDATE  log " +
                "       SET log_message=? " +
                "         WHERE id=?";

        PreparedStatement statement = connection.prepareStatement(sqlString);
        statement.setString(1, logMessage);
        statement.setInt(2,log.getId());
        statement.executeUpdate();
    }

    /**
     * Add new log row to database and return a log instance with corresponding Id.
     *
     * @return              Log instance of inserted row.
     * @throws SQLException
     */

    public Log writeLog() throws SQLException {

        Log log = new Log();

        //Update revision in DB.
        String sqlString = "INSERT INTO log " +
                "       (log_message) " +
                "         VALUES (?)";

        PreparedStatement statement = connection.prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, "");
        statement.executeUpdate();

        //Get the ID of the inserted Run
        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()) {
            log.setId(rs.getInt(1));
        }

        return log;
    }

    /**
     * This method return all error types from the database.
     *
     * @return list of all error types.
     * @throws SQLException
     */

    public List<Error> getAllErrors() throws SQLException {
        List<Error> errors = new ArrayList<>();
        String sqlString = "SELECT * FROM error";

        PreparedStatement statement = connection.prepareStatement(sqlString);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            errors.add(new Error(resultSet.getInt(1), resultSet.getString(2)));
        }
        return errors;
    }
}
