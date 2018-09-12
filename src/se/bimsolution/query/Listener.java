package se.bimsolution.query;

import org.bimserver.client.BimServerClient;
import org.bimserver.emf.IfcModelInterface;
import org.bimserver.interfaces.objects.SProject;
import org.bimserver.shared.exceptions.ServerException;
import org.bimserver.shared.exceptions.UserException;
import org.omg.CORBA.INTERNAL;
import se.bimsolution.db.Repository;
import se.bimsolution.query.machine.*;

import java.sql.SQLException;
import java.util.*;

public class Listener {


    private Repository repo;
    private BimServerClient bsc;
    private final int interval;


    public Listener(BimServerClient bsc, Repository repo, int interval) {
        this.bsc = bsc;
        this.repo = repo;
        this.interval = interval;
    }

    private void runMachines(IfcModelInterface model, Repository repo, int revisionId) {
        List<Class> classList = QueryUtils.getStandardClassList();
        new WrongStoreyChecker(model, repo, revisionId, classList).run();
        new PropertySetMissingChecker(model, repo, revisionId, classList).run();
        new WrongBSABChecker(model, repo, revisionId, classList).run();
        new AreaChecker(model, repo, revisionId, classList).run();

    }

    public void listen() {
        Set<String> names = repo.getAllRevisionsNames();
        while (true) {
            try {
                List<SProject> projects =
                        bsc.getServiceInterface().getAllProjects(true, true);
                for (SProject project:
                        projects) {
                    if (!names.contains(project.getName())) {
                        names.add(awake(project));
                    }
                }
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ServerException e) {
                e.printStackTrace();
            } catch (UserException e) {
                e.printStackTrace();
            }
        }
    }

    private String awake(SProject project) {
        project.getLastRevisionId();
        IfcModelInterface model = null;
        try {
            model = new ModelBuilder(bsc, project).build();
            int revisionId = repo.writeRevision(project.getName()).getId();
            runMachines(model, repo, revisionId);
            return project.getName();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (UserException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

}

