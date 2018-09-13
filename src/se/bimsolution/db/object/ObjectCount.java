package se.bimsolution.db.object;

public class ObjectCount {
    private int id;
    private int revisionId;
    private String elementCheckerName;
    private int totalCheckedObjects;

    public ObjectCount(int revisionId, String elementCheckerName, int totalCheckedObjects) {
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

    public void setTotalCheckedObjects(int totalCheckedObjects){
        this.totalCheckedObjects = totalCheckedObjects;
    }
}