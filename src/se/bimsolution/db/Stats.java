package se.bimsolution.db;

public class Stats {
    private int id;
    private int objectCount;
    private int failCount;
    private int revisionId;
    private int errorId;

    public Stats(int objectCount, int failCount, int revisionId, int errorId) {
        this.objectCount = objectCount;
        this.failCount = failCount;
        this.revisionId = revisionId;
        this.errorId = errorId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(int revisionId) {
        this.revisionId = revisionId;
    }

    public int getErrorId() {
        return errorId;
    }

    public void setErrorId(int errorId) {
        this.errorId = errorId;
    }
}
