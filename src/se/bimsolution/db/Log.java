package se.bimsolution.db;

public class Log {
    int id;
    String logMessage;
    int revisionId;
    int errorId;

    public Log() {
    }

    public Log(String logMessage) {
        this.logMessage = logMessage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public void setRevisionId(int revisionId) {
        this.revisionId = revisionId;
    }

    public void setErrorId(int errorId) {
        this.errorId = errorId;
    }

}
