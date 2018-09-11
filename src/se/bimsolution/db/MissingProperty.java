package se.bimsolution.db;

public class MissingProperty {
    private int id;
    private long objectId;
    private String ifcBuilding;
    private String ifcStorey;
    private String ifcSite;
    private String ifcType;
    private String name;
    private String currentProperties;
    private String correctProperties;
    private int revisionId;


    public MissingProperty(long objectId, String ifcBuilding, String ifcStorey, String ifcSite, String ifcType, String name, String currentProperties, String correctProperties, int revisionId) {
        this.objectId = objectId;
        this.ifcBuilding = ifcBuilding;
        this.ifcStorey = ifcStorey;
        this.ifcSite = ifcSite;
        this.ifcType = ifcType;
        this.name = name;
        this.currentProperties = currentProperties;
        this.correctProperties = correctProperties;
        this.revisionId = revisionId;
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

    public String getIfcType() {
        return ifcType;
    }

    public String getName() {
        return name;
    }

    public String getCurrentProperties() {
        return currentProperties;
    }

    public String getCorrectProperties() {
        return correctProperties;
    }

    public int getRevisionId() {
        return revisionId;
    }
}
