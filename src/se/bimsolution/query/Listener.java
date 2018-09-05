package se.bimsolution.query;

import org.bimserver.client.BimServerClient;
import org.bimserver.emf.IfcModelInterface;
import org.bimserver.interfaces.objects.SProject;
import se.bimsolution.db.Repository;
import se.bimsolution.query.machine.QueryMachine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Listener implements Runnable {


    private Repository repo;
    private List<QueryMachine> machineList;
    private BimServerClient bsc;
    private HashMap<String, Long> map;

    public Listener(BimServerClient bsc, Repository repo,  int interval, QueryMachine... machineList) {
        this.bsc = bsc;
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
            for (String projectname : hasChanged()) {

                awake(projectname);
            }
        }
    }

    private void awake(String projectname) {
        try {
            IfcModelInterface model = new ModelBuilder(bsc, projectname).build();
            new QueryCoordinator(this.repo, this.machineList).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createProjectList() {
        try {
            List<SProject> list = bsc.getServiceInterface().getAllProjects(false, false);
            list.forEach(x-> map.put(x.getName(), x.getLastRevisionId()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> hasChanged() {
        HashMap<String, Long> tryMap = new HashMap<>();
        List<String> runProjects = new ArrayList<>();
        try {
            List<SProject> list = bsc.getServiceInterface().getAllProjects(false, false);
            list.forEach(x-> tryMap.put(x.getName(),x.getLastRevisionId()));
            map.forEach((key, value) -> {
                if (!tryMap.get(key).equals(value)) {
                    runProjects.add(key);
                }
            });

            return runProjects;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return runProjects;
    }

    @Override
    public void run() {
        listen();
    }
}

