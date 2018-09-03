package se.bimsolution.query;

import se.bimsolution.db.Fail;

import java.util.List;

public interface QueryMachine extends Runnable{
    List<Fail> getResult();
    int getCount();
    void run();
}
