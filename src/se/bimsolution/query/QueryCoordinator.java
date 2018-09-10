package se.bimsolution.query;

import org.bimserver.emf.IfcModelInterface;
import org.bimserver.models.ifc2x3tc1.IfcElement;
import se.bimsolution.db.*;

import java.sql.SQLException;
import java.util.*;

import static se.bimsolution.query.QueryUtils.*;

public class QueryCoordinator implements Runnable {

    private int revisionId;
    Repository repo;
    private IfcModelInterface model;
    List<Class> classList;
    HashMap<ElementChecker, Integer> elementCheckerErrorIdMap;


    public QueryCoordinator(int revisionId, Repository repo, IfcModelInterface model, HashMap<ElementChecker, Integer> elementCheckerErrorIdMap) {
        this.revisionId = revisionId;
        this.repo = repo;
        this.model = model;
        this.classList = getStandardClassList();
        this.elementCheckerErrorIdMap = elementCheckerErrorIdMap;
    }


    public QueryCoordinator(int revisionId, Repository repo, IfcModelInterface model) {
        this.revisionId = revisionId;
        this.repo = repo;
        this.model = model;
        this.classList = getStandardClassList();
        this.elementCheckerErrorIdMap = ElementCheckerUtils.standardElementCheckerMapping();
    }

    /**
     * Runs this queryCoordinator on all the objects defined by classes in the classlist.
     * The method begins by writing all the propertySets to db and returns a map of element-id of pset.
     * It then fetches all the ifcTypes in the database and creates a map of name- id
     * Finally, it calls on the checkAllElementsInHashMap Method and writes the fails to DB
     */
    @Override
    public void run() {
        List<IfcElement> elements = new ArrayList<>();
        for (Class clazz :
                this.classList) {
            elements.addAll(model.getAll(clazz));
        }

        try {
            HashMap<IfcElement, PropertySet> elementPropertySetHashMap = ElementCheckerUtils.newIfcElementToAHPropertySetMap(elements);
            HashMap<IfcElement, Integer> elementIdMap = repo.writePropertySetsReturnsMap(elementPropertySetHashMap);
            HashMap<String, Integer> ifcTypeNameIdMap = repo.getIfcTypeNameIdMap();
            List<Fail> fails = ElementCheckerUtils.checkAllElementsInHashMap(this.revisionId,
                    elementIdMap, ifcTypeNameIdMap, this.elementCheckerErrorIdMap);
            repo.writeAllFails(fails);
        } catch (SQLException e) {
            try {
                repo.writeLog(new Log("Query failed: " + e.getMessage(), this.revisionId));
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

    }
}
