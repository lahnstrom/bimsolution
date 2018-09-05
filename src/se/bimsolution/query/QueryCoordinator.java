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

    public QueryCoordinator(Repository repo, List<QueryMachine> machineList) {
        this.repo = repo;
        this.machineList = machineList;
    }

    @Override
    public void run() {
        Run run = repo.newRun();
        run.setSuccess(true);
        boolean hasError = false;

        for (QueryMachine qm:
             machineList) {
            qm.run();
            if (qm.getError()==null) {
                repo.writeAllFails(qm.getFails());
                repo.writeCount(new Count(qm.getCount(), qm.getFailCount(), run.getId(), qm.getID()));

            } else {
                hasError = true;
                repo.writeLog(new Log(1,qm.getError(),1,1));
            }
        }
        run.setSuccess(!hasError);
        repo.updateRun(run);

    }
}
