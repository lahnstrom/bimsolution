package se.bimsolution.query;

import org.bimserver.client.BimServerClient;
import org.bimserver.emf.IfcModelInterface;
import se.bimsolution.db.*;
import se.bimsolution.query.machine.QueryMachine;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueryCoordinator implements Runnable {

    private final Repository repo;
    private final List<QueryMachine> machineList;
    private Log log;

    public QueryCoordinator(Repository repo, QueryMachine... machineList) {
        this.repo = repo;
        this.machineList = Arrays.asList(machineList);
    }

    public QueryCoordinator(Repository repo, List<QueryMachine> machineList) {
        this.repo = repo;
        this.machineList = machineList;
    }

    @Override
    public void run() {
        try {
            log = repo.writeLog();
            Revision revision = repo.writeRevision(123, "mTest");
                repo.writeRevisionIdToLog(log,revision.getId());

            for (QueryMachine qm :
                    machineList) {
                qm.run();
                Stats stats = new Stats(qm.getCount(), qm.getFailCount(), revision.getId(), qm.getErrorId());
                repo.writeAllFails(qm.getFails());
                repo.writeStats(stats);
                repo.writeErrorIdToLog(log,qm.getErrorId());
                repo.writeLogMessageIdToLog(log,"Sucessful execution");
            }
        } catch (Exception e) {

            try {
                e.printStackTrace();
                repo.writeLogMessageIdToLog(log, e.getMessage());
            } catch (SQLException logE) {
                logE.printStackTrace();
                System.out.println(logE.getStackTrace());
            }
        }
    }
}
