package se.bimsolution.db;

import java.sql.SQLException;
import java.util.List;

public interface Repository {
    Revision writeRevision(int projectId, String model) throws SQLException;

    void writeAllFails(List<Fail> fails) throws SQLException;

    Stats writeStats(Stats stats) throws SQLException;

    Log writeLog() throws SQLException;

    void writeRevisionIdToLog(Log log, int revisionId) throws SQLException;

    void writeErrorIdToLog(Log log, int errorId) throws SQLException;

    void writeLogMessageIdToLog(Log log, String logMessage) throws SQLException;

    List<Error> getAllErrors() throws SQLException;
}
