package se.bimsolution.query;

import org.bimserver.client.BimServerClient;
import org.bimserver.emf.IfcModelInterface;
import se.bimsolution.db.Repository;

import java.util.Arrays;
import java.util.List;

public class Listener implements Runnable {

    private ClientBuilder cb;
    private ModelBuilder mb;
    private Repository repo;
    private List<QueryMachine> machineList;

    public Listener(ClientBuilder cb, ModelBuilder mb, Repository repo,  int interval, QueryMachine... machineList) {
        this.cb = cb;
        this.mb = mb;
        this.repo = repo;
        this.machineList = Arrays.asList(machineList);
        this.interval = interval;
    }

    private final int interval;

    public void listen() {
        while (true) {
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (hasChanged()) {
                awake();
            }
        }
    }

    private void awake() {
        try {
            BimServerClient client = cb.build();
            IfcModelInterface model = mb.build();
            new QueryCoordinator(model, client, this.repo, this.machineList).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hasChanged() {
        return false;
    }

    @Override
    public void run() {
        listen();
    }
}

