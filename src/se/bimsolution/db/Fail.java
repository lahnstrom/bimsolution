package se.bimsolution.db;

public class Fail {
    private long objectId;
    private int queryId;
    private int runId;


    public Fail(long objectId, int queryId, int runId) {
        this.objectId = objectId;
        this.queryId = queryId;
        this.runId = runId;
    }


}
