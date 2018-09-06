package se.bimsolution.db;

import java.sql.SQLException;
import java.util.List;

public interface Repository {
    void writeAllFails(List<Fail> fails);

    /**
     * Creates a new Run in the DB and sends the generated key back
     *
     * @param
     * @return
     */
    Revision newRevision() throws SQLException;
    void createLog(Log log);
    void updateRevision(Revision revision);
    Log createLog(int revisionId, int errorId, String logMessage) throws SQLException;
}
