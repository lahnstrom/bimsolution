package se.bimsolution.query.checker;

import org.bimserver.emf.IfcModelInterface;
import org.bimserver.models.ifc2x3tc1.IfcElement;
import se.bimsolution.db.repo.Repository;

import java.util.List;

abstract public class ElementChecker implements Runnable {
    protected IfcModelInterface model;
    protected Repository repo;
    protected int revisionId;
    protected int count;
    protected List<Class<? extends IfcElement>> classList;

    public ElementChecker(IfcModelInterface model, Repository repo, int revisionId, List<Class<? extends IfcElement>> classList) {
        this.model = model;
        this.repo = repo;
        this.revisionId = revisionId;
        this.classList = classList;

    }
}
