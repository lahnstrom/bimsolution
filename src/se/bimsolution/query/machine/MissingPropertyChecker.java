package se.bimsolution.query.machine;

import org.bimserver.emf.IfcModelInterface;
import se.bimsolution.db.Repository;

import java.util.List;

public class MissingPropertyChecker extends ElementChecker {

    public MissingPropertyChecker(IfcModelInterface model, Repository repo, int revisionId, List<Class> classList) {
        super(model, repo, revisionId, classList);
    }

    @Override
    public void run() {

    }
}
