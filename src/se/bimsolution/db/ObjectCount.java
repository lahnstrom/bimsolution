package se.bimsolution.db;

public class ObjectCount {
    private int id;
    private int revisionId;
    private String elementCheckerName;
    private int totalCheckedObjects;

    public ObjectCount(int id, int revisionId, String elementCheckerName, int totalCheckedObjects) {
        this.id = id;
        this.revisionId = revisionId;
        this.elementCheckerName = elementCheckerName;
        this.totalCheckedObjects = totalCheckedObjects;
    }

    public int getId() {
        return id;
    }

    public int getRevisionId() {
        return revisionId;
    }

    public String getElementCheckerName() {
        return elementCheckerName;
    }

    public int getTotalCheckedObjects() {
        return totalCheckedObjects;
    }
}
