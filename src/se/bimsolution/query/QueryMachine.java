package se.bimsolution.query;

import se.bimsolution.db.Fail;

import java.util.List;

public interface QueryMachine extends Runnable{
    List<Fail> getFails();
    int getCount();
    int getFailCount();
    String getError();
    void run();
}
