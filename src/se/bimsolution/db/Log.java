package se.bimsolution.db;

public class Log {
    int id;
    String logMessage;
    int revisionId;
    int errorId;

    public Log(int id, String logMessage, int revisionId, int errorId) {
        this.id = id;
        this.logMessage = logMessage;
        this.revisionId = revisionId;
        this.errorId = errorId;
    }

    public int getId() {
        return id;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public int getRevisionId() {
        return revisionId;
    }

    public int getErrorId() {
        return errorId;
    }
}
