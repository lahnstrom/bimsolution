package se.bimsolution.db;

public class Log {
    int ID;
    String logMessage;
    int runID;
    int qID;

    public Log(int ID, String logMessage, int runID, int qID) {
        this.ID = ID;
        this.logMessage = logMessage;
        this.runID = runID;
        this.qID = qID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }

    public int getRunID() {
        return runID;
    }

    public void setRunID(int runID) {
        this.runID = runID;
    }

    public int getqID() {
        return qID;
    }

    public void setqID(int qID) {
        this.qID = qID;
    }
}
