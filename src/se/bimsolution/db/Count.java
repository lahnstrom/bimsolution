package se.bimsolution.db;

public class Count {
    private int ID;
    private int objectCount;
    private int failCount;
    private int runID;
    private int qID;

    public Count(int objectCount, int failCount, int runID, int qID) {
        this.objectCount = objectCount;
        this.failCount = failCount;
        this.runID = runID;
        this.qID = qID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getObjectCount() {
        return objectCount;
    }

    public void setObjectCount(int objectCount) {
        this.objectCount = objectCount;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public int getRunID() {
        return runID;
    }

    public void setRunID(int runID) {
        this.runID = runID;
    }

    public int getQID() {
        return qID;
    }

    public void setQID(int qID) {
        this.qID = qID;
    }
}
