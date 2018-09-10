package se.bimsolution.db;

import org.bimserver.models.ifc2x3tc1.IfcElement;

import java.sql.*;
import java.util.*;

public class PostgresRepository implements Repository {
    private Connection connection;

    public PostgresRepository(String url, String username, String password) throws RuntimeException, SQLException {
        connection = DriverManager.getConnection(url, username, password);
    }

    /**
     * This method creates a new revision instance and inserts it into the revision table.
     *
     * @return A new Revision instance corresponding to inserted row in the revision table.
     * @throws SQLException
     */
    @Override
    public Revision writeRevision(int projectId, String model) throws SQLException {

        Revision revision = new Revision(projectId, model);
        String sqlString = "INSERT INTO revision " +
                "       (model, project_id) " +
                "         VALUES (?,?)";

        PreparedStatement statement = connection.prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, model);
        statement.setInt(2, projectId);
        statement.executeUpdate();

        ResultSet resultSet = statement.getGeneratedKeys();
        if (resultSet.next()) {
            int id = resultSet.getInt(1);
            Timestamp timestamp = resultSet.getTimestamp(2);
            revision.setId(id);
            revision.setDate(timestamp);
        }
        return revision;
    }

    /**
     * Given a list of ifcTypes, writes the content of that list to DB.
     *
     * @param ifcTypes A list of IfcType instances
     * @throws SQLException
     */
    public void writeIfcTypes(List<IfcType> ifcTypes) throws SQLException {

        String sqlString = "INSERT INTO ifc_type " +
                "       (ifc_name, valid_bsab) " +
                "         VALUES (?,?)";
        PreparedStatement statement = connection.prepareStatement(sqlString);

        for (IfcType type : ifcTypes) {
            statement.setString(1, type.getName());
            statement.setString(2, type.getValidBSAB96BD());
            statement.addBatch();
        }
        statement.executeBatch();
    }

    /**
     * This method creates a new stats instance and inserts it into the stats table.
     *
     * @param stats Stats instance to be inserted into stats table.
     * @throws SQLException
     */
    @Override
    public Stats writeStats(Stats stats) throws SQLException {

        String countSQL = "INSERT INTO stats " +
                "       (object_count, fail_count, revision_id, error_id) " +
                "         VALUES (?,?,?,?)";

        PreparedStatement statement = connection.prepareStatement(countSQL, Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, stats.getObjectCount());
        statement.setInt(2, stats.getFailCount());
        statement.setInt(3, stats.getRevisionId());
        statement.setInt(4, stats.getErrorId());
        statement.executeUpdate();

        ResultSet resultSet = statement.getGeneratedKeys();
        if (resultSet.next()) {
            stats.setId(resultSet.getInt(1));
        }
        return stats;
    }

    /**
     * This method writes all fails from a list of fails to the fail table.
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
//            statement.setString(4, fail.getIfcType());
            statement.setString(5, fail.getIfcSite());
            statement.setString(6, fail.getIfcBuilding());
            statement.setString(7, fail.getIfcStorey());
//            statement.setString(8, fail.getpSetBenamning());
//            statement.setString(9, fail.getpSetBetackning());
//            statement.setString(10, fail.getpSetTypeId());
//            statement.setString(11, fail.getpSetIfylltBsab());
//            statement.setString(12, fail.getpSetGiltigBsab());
//            statement.setString(13, fail.getpSetParameterSomSaknas());
            statement.addBatch();
        }
        statement.executeBatch();
    }


    /**
     * Given a map of IfcElement and corresponding PropertySets, writes the property sets to the DB and returns
     * a hash map of ifc element and the id of the generated property set.
     * @param elementPropertySetMap A map of IfcElement - PropertySet
     * @return A map of IfcElement - ID of PropertySet in DB
     * @throws SQLException
     */
    public HashMap<IfcElement, Integer> writePropertySetsReturnsMap(HashMap<IfcElement, PropertySet> elementPropertySetMap) throws SQLException {
        HashMap<IfcElement, Integer> resultmap = new HashMap<>();
        String sqlString = "INSERT INTO property_set " +
                "       (benamning, beteckning, typ_id, bsab96bd) " +
                "         VALUES (?,?,?,?)";


        for (Map.Entry<IfcElement, PropertySet> entry : elementPropertySetMap.entrySet()) {
            PreparedStatement statement = connection.prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);
            PropertySet pset = entry.getValue();
            statement.setString(1, pset.getBenamning());
            statement.setString(2, pset.getBeteckning());
            statement.setString(3, pset.getTypId());
            statement.setString(4, pset.getBSAB96BD());
            statement.execute();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                resultmap.put(entry.getKey(), rs.getInt("id"));
            }

        }

        return resultmap;
    }

    /**
     * This method insert the provided revision Id to corresponding row in the log table.
     *
     * @param log        Log instance to be used to update the corresponding row in the log table.
     * @param revisionId Revision Id of corresponding revision.
     * @throws SQLException
     */
    @Override
    public void writeRevisionIdToLog(Log log, int revisionId) throws SQLException {

        String sqlString = "UPDATE  log " +
                "       SET revision_id=? " +
                "         WHERE id=?";

        PreparedStatement statement = connection.prepareStatement(sqlString);
        statement.setInt(1, revisionId);
        statement.setInt(2, log.getId());
        statement.executeUpdate();
    }

    /**
     * This method insert error Id to corresponding row into the log table.
     *
     * @param log     Log instance to be used to update the corresponding row in the log table.
     * @param errorId Error Id of corresponding revision.
     * @throws SQLException
     */
    @Override
    public void writeErrorIdToLog(Log log, int errorId) throws SQLException {

        String sqlString = "UPDATE  log " +
                "       SET error_id=? " +
                "         WHERE id=?";

        PreparedStatement statement = connection.prepareStatement(sqlString);
        statement.setInt(1, errorId);
        statement.setInt(2, log.getId());
        statement.executeUpdate();
    }

    /**
     * This method inserts a log message into the row in the log table corresponding to the provided
     * Log instance.
     *
     * @param log        Log instance to be used to update the corresponding row in the log table.
     * @param logMessage Log message of corresponding revision.
     * @throws SQLException
     */
    @Override
    public void writeLogMessageIdToLog(Log log, String logMessage) throws SQLException {

        String sqlString = "UPDATE  log " +
                "       SET log_message=? " +
                "         WHERE id=?";

        PreparedStatement statement = connection.prepareStatement(sqlString);
        statement.setString(1, logMessage);
        statement.setInt(2, log.getId());
        statement.executeUpdate();
    }

    /**
     * Add new log row to database and return a log instance with corresponding Id.
     *
     * @return Log instance of inserted row.
     * @throws SQLException
     */
    public Log writeLog() throws SQLException {

        Log log = new Log();

        String sqlString = "INSERT INTO log " +
                "       (log_message) " +
                "         VALUES (?)";

        PreparedStatement statement = connection.prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, "");
        statement.executeUpdate();

        ResultSet resultSet = statement.getGeneratedKeys();
        if (resultSet.next()) {
            log.setId(resultSet.getInt(1));
        }
        return log;
    }

    /**
     * This method returns all error types from the database.
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
