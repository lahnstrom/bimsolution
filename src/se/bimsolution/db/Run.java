package se.bimsolution.db;

public class Run {
    int id;
    int count1;
    int count2;
    int count3;
    int count4;
    boolean success;

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
