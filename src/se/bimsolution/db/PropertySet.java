package se.bimsolution.db;

public class PropertySet {
    private int id;
    private String benamning;
    private String beteckning;
    private int typId;
    private String BSAB96BD;

    public PropertySet(String benamning, String beteckning, int typId, String BSAB96BD) {
        this.benamning = benamning;
        this.beteckning = beteckning;
        this.typId = typId;
        this.BSAB96BD = BSAB96BD;
    }

    public int getId() {
        return id;
    }

    public String getBenamning() {
        return benamning;
    }

    public String getBeteckning() {
        return beteckning;
    }

    public int getTypId() {
        return typId;
    }

    public String getBSAB96BD() {
        return BSAB96BD;
    }
}