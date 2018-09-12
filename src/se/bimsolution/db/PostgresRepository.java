package se.bimsolution.db;

import it.unimi.dsi.fastutil.Hash;
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
            statement.setString(5, fail.getIfcSite());
            statement.setString(6, fail.getIfcBuilding());
            statement.setString(7, fail.getIfcStorey());
            statement.addBatch();
        }
        statement.executeBatch();
    }


    @Override
    public void writeArea(List<Area> areas) throws SQLException {
        String sqlString = "INSERT INTO area " +
                "       (object_id, ifc_building, ifc_storey, ifc_site, area, revision_id, name) " +
                "         VALUES (?,?,?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sqlString);

        for (Area area : areas) {
            statement.setLong(1, area.getObjectId());
            statement.setString(2, area.getIfcBuilding());
            statement.setString(3, area.getIfcStorey());
            statement.setString(4, area.getIfcSite());
            statement.setDouble(5, area.getArea());
            statement.setInt(6, area.getRevisionId());
            statement.setString(7, area.getName());
            statement.addBatch();
        }
        statement.executeBatch();
    }

    @Override
    public void writeBsab96bdMissing(List<Bsab96bdMissing> missings) throws SQLException {
        String sqlString = "INSERT INTO bsab96bd_missing " +
                "(object_id, ifc_building, ifc_storey, ifc_type, ifc_site, revision_id, name) values (?,?,?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sqlString);

        for (Bsab96bdMissing missing : missings) {
            statement.setLong(1, missing.getObjectId());
            statement.setString(2, missing.getIfcBuilding());
            statement.setString(3, missing.getIfcStorey());
            statement.setString(4, missing.getIfcType());
            statement.setString(5, missing.getIfcSite());
            statement.setInt(6, missing.getRevisionId());
            statement.setString(7, missing.getName());
            statement.addBatch();
        }
        statement.executeBatch();
    }

    @Override
    public void writeBsab96bdWrong(List<Bsab96bdWrong> wrongs) throws SQLException {
        String sqlString = "INSERT INTO bsab96bd_wrong (object_id, ifc_building, ifc_storey, ifc_type, current_bsab, correct_bsab, ifc_site, revision_id, name) "
                + " values (?,?,?,?,?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sqlString);

        for (Bsab96bdWrong wrong : wrongs) {
            statement.setLong(1, wrong.getObjectId());
            statement.setString(2, wrong.getIfcBuilding());
            statement.setString(3, wrong.getIfcStorey());
            statement.setString(4, wrong.getIfcType());
            statement.setString(5, wrong.getCurrentBsab());
            statement.setString(6, wrong.getCorrectBsab());
            statement.setString(7, wrong.getIfcSite());
            statement.setInt(8, wrong.getRevisionId());
            statement.setString(9, wrong.getName());
            statement.addBatch();
        }
        statement.executeBatch();
    }


    @Override
    public void writeMissingProperty(List<MissingProperty> missings) throws SQLException {
        String sqlString = "INSERT INTO missing_property " +
                "(object_id, ifc_building, ifc_storey, ifc_site," +
                " ifc_type, name, current_properties, correct_properties,  revision_id)" +
                " values (?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sqlString);

        for (MissingProperty missing : missings) {
            statement.setLong(1, missing.getObjectId());
            statement.setString(2, missing.getIfcBuilding());
            statement.setString(3, missing.getIfcStorey());
            statement.setString(4, missing.getIfcSite());
            statement.setString(5, missing.getIfcType());
            statement.setString(6, missing.getName());
            statement.setString(7, missing.getCurrentProperties());
            statement.setString(8, missing.getCorrectProperties());
            statement.setInt(9, missing.getRevisionId());
            statement.addBatch();
        }
        statement.executeBatch();
    }

    @Override
    public void writeMissingPropertySet(List<MissingPropertySet> missings) throws SQLException {
        String sqlString = "INSERT INTO missing_property_set " +
                "(object_id, ifc_building, ifc_storey, ifc_site, ifc_type, revision_id, name)" +
                " values (?,?,?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sqlString);

        for (MissingPropertySet missing : missings) {
            statement.setLong(1, missing.getObjectId());
            statement.setString(2, missing.getIfcBuilding());
            statement.setString(3, missing.getIfcStorey());
            statement.setString(4, missing.getIfcSite());
            statement.setString(5, missing.getIfcType());
            statement.setInt(6, missing.getRevisionId());
            statement.setString(7, missing.getName());
            statement.addBatch();
        }
        statement.executeBatch();
    }

    @Override
    public void writeWrongStorey(List<WrongStorey> wrongs) throws SQLException {
        String sqlString = "INSERT INTO wrong_storey " +
                "(object_id, ifc_type, ifc_storey, ifc_building, ifc_site, revision_id, name)" +
                " values (?,?,?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sqlString);

        for (WrongStorey storey : wrongs) {
            statement.setLong(1, storey.getObjectId());
            statement.setString(2, storey.getIfcType());
            statement.setString(3, storey.getIfcStorey());
            statement.setString(4, storey.getIfcBuilding());
            statement.setString(5, storey.getIfcSite());
            statement.setInt(6, storey.getRevisionId());
            statement.setString(7, storey.getName());
            statement.addBatch();
        }
        statement.executeBatch();
    }

    /**
     * Given a map of IfcElement and corresponding PropertySets, writes the property sets to the DB and returns
     * a hash map of ifc element and the id of the generated property set.
     *
     * @param elementPropertySetMap A map of IfcElement - PropertySet
     * @return A map of IfcElement - ID of PropertySet in DB
     * @throws SQLException
     */
    public HashMap<IfcElement, Integer> writePropertySetsReturnsMap(HashMap<IfcElement, PropertySet> elementPropertySetMap) throws SQLException {
        HashMap<IfcElement, Integer> resultmap = new HashMap<>();
        String sqlString = "INSERT INTO property_set " +
                "       (benamning, beteckning, typ_id, bsab96bd) " +
                "         VALUES (?,?,?,?)";
        HashMap<IfcElement, PropertySet> itermap = new LinkedHashMap<>(elementPropertySetMap);
        PreparedStatement statement = connection.prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);


        for (Map.Entry<IfcElement, PropertySet> entry : itermap.entrySet()) {
            PropertySet pset = entry.getValue();
            if (pset != null) {
                statement.setString(1, pset.getBenamning());
                statement.setString(2, pset.getBeteckning());
                statement.setString(3, pset.getTypId());
                statement.setString(4, pset.getBSAB96BD());
                statement.addBatch();
            }
        }
        statement.executeLargeBatch();
        ResultSet genKeys = statement.getGeneratedKeys();
        genKeys.next();
        //OBS, Detta kommer göra sig av med alla element som har null property set
        for (Map.Entry<IfcElement, PropertySet> entry : itermap.entrySet()) {
            if (entry.getValue() != null) {
                resultmap.put(entry.getKey(), genKeys.getInt("id"));
                genKeys.next();
            } else {
                resultmap.put(entry.getKey(), 0);
            }
        }


        return resultmap;
    }

    /**
     * Reads the Ifc_Type table and creates a hashmap with ifc_name - id, used when creating new Fails.
     *
     * @return A new HashMap of Ifc_name - Id
     * @throws SQLException
     */
    @Override
    public HashMap<String, Integer> getIfcTypeNameIdMap() throws SQLException {
        HashMap<String, Integer> ifcTypeNameIdMap = new HashMap<>();
        String sqlString = "SELECT id, ifc_name FROM ifc_type";

        PreparedStatement statement = connection.prepareStatement(sqlString);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            ifcTypeNameIdMap.put(resultSet.getString("ifc_name"), resultSet.getInt("id"));
        }
        return ifcTypeNameIdMap;
    }

    /**
     * This method inserts the provided revision Id to corresponding row in the log table.
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
     * This method inserts error Id to corresponding row in the log table.
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

    public void writeLog(Log log) {
        try {
            String sqlString = "INSERT INTO log " +
                    "       (log_message, revision_id) " +
                    "         VALUES (?, ?)";

            PreparedStatement statement = connection.prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, log.getLogMessage());
            statement.setInt(2, log.getRevisionId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void writeObjectCount(ObjectCount objectCount){
        try {
            String sqlString = "INSERT INTO object_count " +
                    "       (revision_id, element_checker_name, total_checked_objects) " +
                    "         VALUES (?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, objectCount.getRevisionId());
            statement.setString(2, objectCount.getElementCheckerName());
            statement.setInt(3,objectCount.getTotalCheckedObjects());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @Override
    public void close() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
