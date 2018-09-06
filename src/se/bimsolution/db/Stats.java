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

    public int getFailCount() {
        return failCount;
    }

    public int getRevisionId() {
        return revisionId;
    }

    public int getErrorId() {
        return errorId;
    }

}
