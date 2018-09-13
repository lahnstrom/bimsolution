//package se.bimsolution.query;
//
//import org.bimserver.emf.IfcModelInterface;
//import org.bimserver.models.ifc2x3tc1.IfcElement;
//import se.bimsolution.db.*;
//
//import javax.xml.bind.Element;
//import java.util.*;
//
//import static se.bimsolution.query.QueryUtils.*;
//
//public class QueryCoordinator implements Runnable {
//
////    private List<ElementChecker> checkers;
////
////    public QueryCoordinator( List<ElementChecker> checkers) {
////        this.checkers = checkers;
////    }
////    private Repository repo;
////    private int revisionId;
////    private IfcModelInterface model;
////    private List<Class> classList;
////    private HashMap<IElementChecker, Integer> elementCheckerErrorIdMap;
////
////    public QueryCoordinator(int revisionId, Repository repo, IfcModelInterface model) {
////        this.revisionId = revisionId;
////        this.repo = repo;
////        this.model = model;
////        this.classList = getElementClassList();
////        this.elementCheckerErrorIdMap = ElementCheckerUtils.standardElementCheckerMapping();
////    }
////
////    /**
////     * Runs this queryCoordinator on all the objects defined by classes in the classlist.
////     * The method begins by writing all the propertySets to db and returns a map of element-id of pset.
////     * It then fetches all the ifcTypes in the database and creates a map of name- id
////     * Finally, it calls on the checkAllElementsInHashMap Method and writes the fails to DB
////     */
////    @Override
////    public void run() {
////        for (ElementChecker checker:
////             checkers) {
////            checker.run();
////        }
////
////        try {
////            HashMap<IfcElement, PropertySet> elementPropertySetHashMap = ElementCheckerUtils.newIfcElementToAHPropertySetMap(elements);
////            HashMap<IfcElement, Integer> elementIdMap = repo.writePropertySetsReturnsMap(elementPropertySetHashMap);
////            HashMap<String, Integer> ifcTypeNameIdMap = repo.getIfcTypeNameIdMap();
////            List<Fail> fails = ElementCheckerUtils.checkAllElementsInHashMap(this.revisionId,
////                    elementIdMap, ifcTypeNameIdMap, this.elementCheckerErrorIdMap);
////            repo.writeAllFails(fails);
////        } catch (SQLException e) {
////
////            repo.writeLog(new Log("Query failed: " + e.getMessage(), this.revisionId));
////        }
////    }
//}
