package se.bimsolution.db;

public class Area {
    private int id;
    private long objectId;
    private String ifcBuilding;
    private String ifcStorey;
    private String ifcSite;
    private double area;
    private int revisionId;
    private String name;

    public Area(long objectId, String ifcBuilding, String ifcStorey, String ifcSite, double area, int revisionId, String name) {
        this.objectId = objectId;
        this.ifcBuilding = ifcBuilding;
        this.ifcStorey = ifcStorey;
        this.ifcSite = ifcSite;
        this.area = area;
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

    public String getIfcSite() {
        return ifcSite;
    }

    public double getArea() {
        return area;
    }

    public int getRevisionId() {
        return revisionId;
    }

    public String getName() {
        return name;
    }
}
