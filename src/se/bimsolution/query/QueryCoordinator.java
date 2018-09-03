package se.bimsolution.query;

import org.bimserver.client.BimServerClient;
import org.bimserver.emf.IfcModelInterface;
import se.bimsolution.db.Repository;

import java.util.List;

public class QueryCoordinator implements Runnable{


    private final IfcModelInterface model;
    private final BimServerClient client;
    private final Repository repo;
    private final List<QueryMachine> machineList;

    QueryCoordinator(IfcModelInterface model, BimServerClient client, Repository repo, List<QueryMachine> machineList) {

        this.model = model;
        this.client = client;
        this.repo = repo;
        this.machineList = machineList;
    }

    @Override
    public void run() {

    }
}
