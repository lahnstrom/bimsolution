package se.bimsolution.db;

import java.util.List;

public interface Repository {
    void writeAllFails(List<Fail> fails);

    /**
     * Creates a new Run in the DB and sends the generated key back
     * @param
     * @return
     */
    Run newRun();
    void writeCount(Count count);
    void writeLog(Log log);
    void updateRun(Run run);

}
