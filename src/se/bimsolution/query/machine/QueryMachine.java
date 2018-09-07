package se.bimsolution.query.machine;

import se.bimsolution.db.Fail;

import java.util.List;

public interface QueryMachine extends Runnable{
    int getID();
    List<Fail> getFails();
    int getCount();
    int getFailCount();
    String getError();
    void run();
    int getErrorId();
}
