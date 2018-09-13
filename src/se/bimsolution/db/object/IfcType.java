package se.bimsolution.db.object;

public class IfcType {
    private int id;
    private String name;
    private String validBSAB96BD;

    public IfcType(String name, String validBSAB96BD) {
        this.name = name;
        this.validBSAB96BD = validBSAB96BD;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getValidBSAB96BD() {
        return validBSAB96BD;
    }
}
