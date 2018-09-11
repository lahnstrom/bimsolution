package se.bimsolution.query.machine;

import org.bimserver.emf.IfcModelInterface;
import se.bimsolution.db.Repository;

abstract public class ElementChecker implements Runnable{
    private IfcModelInterface model;
    private Repository repo;

    public ElementChecker(IfcModelInterface model, Repository repo) {
        this.model = model;
        this.repo = repo;
    }


}
