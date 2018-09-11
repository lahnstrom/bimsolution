package se.bimsolution.db;

public class Bsab96bdMissing {
    private int id;
    private long objectId;
    private String ifcBuilding;
    private String ifcStorey;
    private String ifcType;
    private String ifcSite;
    private int revisionId;
    private String name;

    public Bsab96bdMissing(long objectId, String ifcBuilding, String ifcStorey, String ifcType, String ifcSite, int revisionId, String name) {
        this.objectId = objectId;
        this.ifcBuilding = ifcBuilding;
        this.ifcStorey = ifcStorey;
        this.ifcType = ifcType;
        this.ifcSite = ifcSite;
        this.revisionId = revisionId;
        this.name = name;
    }


    public String getIfcType() {
        return ifcType;
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
