package se.bimsolution.query.mock;

import se.bimsolution.db.Fail;
import se.bimsolution.query.QueryMachine;

import java.util.ArrayList;
import java.util.List;

public class mockQueryMachine implements QueryMachine {
    final int ID = 0;

    @Override
    public List<Fail> getFails() {
        List<Fail> retList = new ArrayList<>();
        retList.add(new Fail(123, 1, 1));
        retList.add(new Fail(123, 1, 1));
        retList.add(new Fail(125, 1, 1));
        retList.add(new Fail(123, 1, 1));
        retList.add(new Fail(123, 1, 1));
        retList.add(new Fail(123, 1, 1));
        return retList;
    }
    @Override
    public int getID(){
        return ID;
    }

    @Override
    public int getCount() {
        return 20;
    }

    @Override
    public int getFailCount() {
        return 6;
    }

    @Override
    public String getError() {
        return null;
    }

    @Override
    public void run() {
    }
}
