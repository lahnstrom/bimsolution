package se.bimsolution.query.machine;

import org.bimserver.emf.IfcModelInterface;
import se.bimsolution.db.Repository;

abstract public class ElementChecker implements Runnable {
    protected IfcModelInterface model;
    protected Repository repo;
    protected int revisionId;
    protected int count;

    public ElementChecker(IfcModelInterface model, Repository repo, int revisionId) {
        this.model = model;
        this.repo = repo;
        this.revisionId = revisionId;
    }

}
