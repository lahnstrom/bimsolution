package se.bimsolution.db;

public class MissingPropertySet {
    private int id;
    private long objectId;
    private String ifcBuilding;
    private String ifcStorey;
    private String ifcType;
    private int revisionId;
    private String name;

    public MissingPropertySet(long objectId, String ifcBuilding, String ifcStorey, String ifcType, int revisionId, String name) {
        this.objectId = objectId;
        this.ifcBuilding = ifcBuilding;
        this.ifcStorey = ifcStorey;
        this.ifcType = ifcType;
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

    public int getRevisionId() {
        return revisionId;
    }

    public String getName() {
        return name;
    }
}
