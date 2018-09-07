package se.bimsolution.query;

import org.bimserver.client.BimServerClient;
import org.bimserver.emf.IfcModelInterface;
import org.bimserver.models.ifc2x3tc1.IfcElement;
import se.bimsolution.db.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static se.bimsolution.query.QueryUtils.*;

public class QueryCoordinator implements Runnable {

    private int revisionId;
    Repository repo;
    private IfcModelInterface model;
    List<ElementChecker> checkers;

    public QueryCoordinator(int revisionId, Repository repo, IfcModelInterface model, ElementChecker... elementCheckers) {
        this.revisionId = revisionId;
        this.repo = repo;
        this.model = model;
        checkers = Arrays.asList(elementCheckers);
    }


    @Override
    public void run() {
        List<Class> classList = standardClassList();
        List<IfcElement> elements = new ArrayList<>();
        for (Class clazz:
             classList) {
            elements.addAll(model.getAll(clazz));
        }
        HashMap<IfcElement, PropertySet> elementPropertySetHashMap = newIfcElementToAHPropertySetMap(elements);
//        checkAllElementsInHashMap(revisionId, )

    }
}
