package se.bimsolution.query.checker;

import org.bimserver.emf.IfcModelInterface;
import org.bimserver.models.ifc2x3tc1.IfcElement;
import se.bimsolution.db.repo.Repository;

import java.util.List;

public class MissingPropertyChecker extends ElementChecker {

    public MissingPropertyChecker(IfcModelInterface model, Repository repo, int revisionId, List<Class<? extends IfcElement>> classList) {
        super(model, repo, revisionId, classList);
    }

    @Override
    public void run() {

    }
}
