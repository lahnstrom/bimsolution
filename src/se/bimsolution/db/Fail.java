package se.bimsolution.db;

public class Fail {
    private int id;
    private int objectId;
    private int revisionId;
    private int errorId;
    private String ifcType;
    private String ifcSite;
    private String ifcBuilding;
    private String ifcStorey;
    private String pSetBenamning;
    private String pSetBetackning;
    private String pSetTypeId;
    private String pSetIfylltBsab;
    private String pSetGiltigBsab;
    private String pSetParameterSomSaknas;

    public Fail(int id, int objectId, int revisionId, int errorId, String ifcType, String ifcSite, String ifcBuilding,
                String ifcStorey, String pSetBenamning, String pSetBetackning, String pSetTypeId, String pSetIfylltBsab,
                String pSetGiltigBsab, String pSetParameterSomSaknas) {
        this.id = id;
        this.objectId = objectId;
        this.revisionId = revisionId;
        this.errorId = errorId;
        this.ifcType = ifcType;
        this.ifcSite = ifcSite;
        this.ifcBuilding = ifcBuilding;
        this.ifcStorey = ifcStorey;
        this.pSetBenamning = pSetBenamning;
        this.pSetBetackning = pSetBetackning;
        this.pSetTypeId = pSetTypeId;
        this.pSetIfylltBsab = pSetIfylltBsab;
        this.pSetGiltigBsab = pSetGiltigBsab;
        this.pSetParameterSomSaknas = pSetParameterSomSaknas;
    }

    public int getId() {
        return id;
    }

    public int getObjectId() {
        return objectId;
    }

    public int getRevisionId() {
        return revisionId;
    }

    public int getErrorId() {
        return errorId;
    }

    public String getIfcType() {
        return ifcType;
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

    public String getpSetBenamning() {
        return pSetBenamning;
    }

    public String getpSetBetackning() {
        return pSetBetackning;
    }

    public String getpSetTypeId() {
        return pSetTypeId;
    }

    public String getpSetIfylltBsab() {
        return pSetIfylltBsab;
    }

    public String getpSetGiltigBsab() {
        return pSetGiltigBsab;
    }

    public String getpSetParameterSomSaknas() {
        return pSetParameterSomSaknas;
    }
}
