package se.bimsolution.db;

public class WrongStorey {
    private int id;
    private long objectId;
    private String ifcStorey;
    private String ifcBuilding;
    private String ifcSite;
    private String ifcType;
    private int revisionId;
    private String name;

    public WrongStorey(long objectId, String ifcStorey, String ifcBuilding, String ifcSite, String ifcType, int revisionId, String name) {
        this.objectId = objectId;
        this.ifcStorey = ifcStorey;
        this.ifcBuilding = ifcBuilding;
        this.ifcSite = ifcSite;
        this.ifcType = ifcType;
        this.revisionId = revisionId;
        this.name = name;
    }

    public long getObjectId() {
        return objectId;
    }

    public String getIfcStorey() {
        return ifcStorey;
    }

    public String getIfcBuilding() {
        return ifcBuilding;
    }

    public String getIfcSite() {
        return ifcSite;
    }

    public String getIfcType() {
        return ifcType;
    }

    public int getRevisionId() {
        return revisionId;
    }

    public String getName() {
        return name;
    }
}
