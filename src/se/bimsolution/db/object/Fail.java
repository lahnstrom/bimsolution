package se.bimsolution.db.object;

public class Fail {
    private int id;
    private long objectId;
    private int revisionId;
    private int errorId;
    private int ifcTypeId;
    private String ifcSite;
    private String ifcBuilding;
    private String ifcStorey;
    private int psetId;


    public Fail(long objectId, int revisionId, int errorId, int ifcTypeId, String ifcSite, String ifcBuilding,
                String ifcStorey, int psetId) {
        this.objectId = objectId;
        this.revisionId = revisionId;
        this.errorId = errorId;
        this.ifcTypeId = ifcTypeId;
        this.ifcSite = ifcSite;
        this.ifcBuilding = ifcBuilding;
        this.ifcStorey = ifcStorey;

    }

    public int getId() {
        return id;
    }

    public long getObjectId() {
        return objectId;
    }

    public int getRevisionId() {
        return revisionId;
    }

    public int getErrorId() {
        return errorId;
    }


    public String getIfcSite() {
        return ifcSite;
    }

    public String getIfcBuilding() {
        return ifcBuilding;
    }

    public String getIfcStorey() {
        return ifcStorey;
    }

    public int getIfcTypeId() {
        return ifcTypeId;
    }

    public int getPsetId() {
        return psetId;
    }
}
