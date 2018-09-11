package se.bimsolution.db;

import org.bimserver.models.ifc2x3tc1.IfcElement;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public interface Repository {
    Revision writeRevision(int projectId, String model) throws SQLException;

    void writeAllFails(List<Fail> fails) throws SQLException;

    Stats writeStats(Stats stats) throws SQLException;

    Log writeLog() throws SQLException;
    void writeLog(Log log) throws SQLException;

    HashMap<IfcElement, Integer> writePropertySetsReturnsMap(HashMap<IfcElement, PropertySet> elementPropertySetMap) throws SQLException;

    HashMap<String, Integer> getIfcTypeNameIdMap() throws SQLException;

    void writeRevisionIdToLog(Log log, int revisionId) throws SQLException;

    void writeErrorIdToLog(Log log, int errorId) throws SQLException;

    void writeLogMessageIdToLog(Log log, String logMessage) throws SQLException;

    List<Error> getAllErrors() throws SQLException;

    void close();
}
