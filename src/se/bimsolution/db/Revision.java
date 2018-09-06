package se.bimsolution.db;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Revision {
    private int id;
    private int projectId;
    private String model;
    private Timestamp localDateTime;

    public Revision(int projectId, String model) {
        this.projectId = projectId;
        this.model = model;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProject(int projectId) {
        this.projectId = projectId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Timestamp getDate() {
        return localDateTime;
    }

    public void setDate(Timestamp localDateTime) {
        this.localDateTime = localDateTime;
    }
}
