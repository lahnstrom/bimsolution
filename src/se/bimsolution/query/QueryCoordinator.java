package se.bimsolution.query;

import org.bimserver.emf.IfcModelInterface;
import se.bimsolution.db.*;

import javax.xml.bind.Element;
import java.util.*;

import static se.bimsolution.query.QueryUtils.*;

public class QueryCoordinator implements Runnable {

    private List<ElementChecker> checkers;

    public QueryCoordinator( List<ElementChecker> checkers) {
        this.checkers = checkers;
    }


    @Override
    public void run() {
        for (ElementChecker checker:
             checkers) {
            checker.run();
        }
    }
}
