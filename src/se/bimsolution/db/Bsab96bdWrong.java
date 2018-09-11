package se.bimsolution.db;

public class Bsab96bdWrong {
    private int id;
    private long objectId;
    private String ifcBuilding;
    private String ifcStorey;
    private String ifcType;
    private String currentBsab;
    private String correctBsab;
    private String ifcSite;
    private int revisionId;
    private String name;

    public Bsab96bdWrong(long objectId, String ifcBuilding, String ifcStorey, String ifcType, String currentBsab, String correctBsab, String ifcSite, int revisionId, String name) {
        this.objectId = objectId;
        this.ifcBuilding = ifcBuilding;
        this.ifcStorey = ifcStorey;
        this.ifcType = ifcType;
        this.currentBsab = currentBsab;
        this.correctBsab = correctBsab;
        this.ifcSite = ifcSite;
        this.revisionId = revisionId;
        this.name = name;
    }

    public long getObjectId() {
        return objectId;
    }

    public String getIfcBuilding() {
        return ifcBuilding;
    }

    public String getIfcStorey() {
        return ifcStorey;
    }

    public String getIfcType() {
        return ifcType;
    }

    public String getCurrentBsab() {
        return currentBsab;
    }

    public String getCorrectBsab() {
        return correctBsab;
    }

    public String getIfcSite() {
        return ifcSite;
    }

    public int getRevisionId() {
        return revisionId;
    }

    public String getName() {
        return name;
    }
}
