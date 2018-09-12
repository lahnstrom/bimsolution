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

    public void writeLog(Log log);

    void writeObjectCount(ObjectCount objectCount);

    void writeArea(List<Area> areas) throws SQLException;

    void writeBsab96bdMissing(List<Bsab96bdMissing> missings) throws SQLException;

    void writeBsab96bdWrong(List<Bsab96bdWrong> wrongs) throws SQLException;

    void writeMissingProperty(List<MissingProperty> missings) throws SQLException;

    void writeMissingPropertySet(List<MissingPropertySet> missings) throws SQLException;

    void writeWrongStorey(List<WrongStorey> wrongs) throws SQLException;

    HashMap<IfcElement, Integer> writePropertySetsReturnsMap(HashMap<IfcElement, PropertySet> elementPropertySetMap) throws SQLException;

    HashMap<String, Integer> getIfcTypeNameIdMap() throws SQLException;

    void writeRevisionIdToLog(Log log, int revisionId) throws SQLException;

    void writeErrorIdToLog(Log log, int errorId) throws SQLException;



    List<Error> getAllErrors() throws SQLException;

    void close();
}
