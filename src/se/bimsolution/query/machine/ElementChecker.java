package se.bimsolution.query.machine;

import org.bimserver.emf.IfcModelInterface;
import se.bimsolution.db.Repository;

import java.util.List;

abstract public class ElementChecker implements Runnable {
    protected IfcModelInterface model;
    protected Repository repo;
    protected int revisionId;
    protected int count;
    List<Class> classList;

    public ElementChecker(IfcModelInterface model, Repository repo, int revisionId, List<Class> classList) {
        this.model = model;
        this.repo = repo;
        this.revisionId = revisionId;
        this.classList = classList;
    }
}
