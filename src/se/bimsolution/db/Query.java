package se.bimsolution.db;

public class Query {
    private int ID;
    private String query;

    public Query(int ID, String query) {
        this.ID = ID;
        this.query = query;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
