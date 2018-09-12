package se.bimsolution.query;

import org.bimserver.emf.IfcModelInterface;
import se.bimsolution.db.*;

import javax.xml.bind.Element;
import java.util.*;

import static se.bimsolution.query.QueryUtils.*;

public class QueryCoordinator implements Runnable {

    private int revisionId;
    private Repository repo;
    private IfcModelInterface model;
    private List<ElementChecker> checkers;

    public QueryCoordinator(int revisionId, Repository repo, IfcModelInterface model, List<ElementChecker> checkers) {
        this.revisionId = revisionId;
        this.repo = repo;
        this.model = model;
        this.checkers = checkers;
    }


    /**
     * Runs this queryCoordinator on all the objects defined by classes in the classlist.
     * The method begins by writing all the propertySets to db and returns a map of element-id of pset.
     * It then fetches all the ifcTypes in the database and creates a map of name- id
     * Finally, it calls on the checkAllElementsInHashMap Method and writes the fails to DB
     */
    @Override
    public void run() {


    }
}
