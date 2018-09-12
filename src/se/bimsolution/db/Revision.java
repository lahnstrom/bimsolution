package se.bimsolution.db;

import java.sql.Timestamp;

public class Revision {
    private int id;
    private int projectId;
    private String model;
    private Timestamp timeStamp;

    public Revision(int projectId, String model) {
        this.projectId = projectId;
        this.model = model;
    }

    public Revision(String model) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }
}
