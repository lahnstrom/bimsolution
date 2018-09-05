package se.bimsolution.query;

import org.bimserver.client.BimServerClient;
import org.bimserver.emf.IfcModelInterface;
import se.bimsolution.db.*;
import se.bimsolution.query.machine.QueryMachine;

import java.util.ArrayList;
import java.util.List;

public class QueryCoordinator implements Runnable{

    private final Repository repo;
    private final List<QueryMachine> machineList;


    QueryCoordinator(Repository repo, List<QueryMachine> machineList) {
        this.repo = repo;
        this.machineList = machineList;
    }

    @Override
    public void run() {
        Run run = repo.newRun();
        boolean hasError = false;
        for (QueryMachine qm:
             machineList) {
            qm.run();
            if (qm.getError()==null) {
                repo.writeAllFails(qm.getFails());
                repo.writeCount(new Count(qm.getCount(), qm.getFailCount()));
            } else {
                hasError = true;
                repo.writeLog(new Log(qm.getError()));
            }
        }
        run.setSuccess(!hasError);
        repo.updateRun(run);

    }
}
