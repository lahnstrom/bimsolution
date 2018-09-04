package se.bimsolution.query.mock;

import se.bimsolution.db.Fail;
import se.bimsolution.query.QueryMachine;

import java.util.ArrayList;
import java.util.List;

public class mockQueryMachine implements QueryMachine {
    @Override
    public List<Fail> getFails() {
        List<Fail> retList = new ArrayList<>();
        retList.add(new Fail(123, 2, 100));
        retList.add(new Fail(123, 7, 100));
        retList.add(new Fail(125, 3, 100));
        retList.add(new Fail(123, 2, 101));
        retList.add(new Fail(123, 2, 101));
        retList.add(new Fail(123, 1, 101));
        return retList;
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
