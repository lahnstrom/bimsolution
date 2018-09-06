package se.bimsolution.db;

public class Revision {
    int id;
    int projectId;
    boolean success;
    String date;


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    public boolean getSuccess() {return this.success;}

}
