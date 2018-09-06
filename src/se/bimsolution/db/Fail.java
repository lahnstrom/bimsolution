package se.bimsolution.db;

public class Fail {
    private int ID;
    private int runID;
    private long objectID;
    private int qID;
    private String ifcType;


    public Fail(long objectID, int qID, int runID, String ifcType) {
        this.objectID = objectID;
        this.qID = qID;
        this.runID = runID;
        this.ifcType = ifcType;
    }




    public int getqID() {
        return qID;
    }

    public String getIfcType() {
        return ifcType;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getRunID() {
        return runID;
    }

    public void setRunID(int runID) {
        this.runID = runID;
    }

    public long getObjectID() {
        return objectID;
    }

    public void setObjectID(int objectID) {
        this.objectID = objectID;
    }

    public int getQID() {
        return qID;
    }

    public void setQID(int qID) {
        this.qID = qID;

    }
}
