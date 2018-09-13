package se.bimsolution.query.machine;

import org.bimserver.emf.IfcModelInterface;
import org.bimserver.models.ifc2x3tc1.IfcElement;
import org.bimserver.models.ifc2x3tc1.IfcPropertySet;
import se.bimsolution.db.*;

import static se.bimsolution.query.QueryUtils.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import se.bimsolution.db.Log;
import se.bimsolution.db.Repository;

import static se.bimsolution.query.ElementCheckerUtils.extractTextValueByNameOfSingleValue;
import static se.bimsolution.query.QueryUtils.*;

import java.sql.SQLException;
import java.util.*;

public class WrongBSABChecker extends ElementChecker {
    ObjectCount objectCount;

    public WrongBSABChecker(IfcModelInterface model, Repository repo, int revisionId, List<Class> classList) {
        super(model, repo, revisionId, classList);
        objectCount = new ObjectCount(revisionId, "WrongBSAB");
    }

    public void run() {
        List<Bsab96bdWrong> bsab96bwrongs = new ArrayList<>();
        List<IfcElement> elements = new ArrayList<>();
        for (Class clazz :
                this.classList) {
            try {
                elements.addAll(model.getAll(clazz));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        objectCount.setTotalCheckedObjects(elements.size());
        repo.writeObjectCount(objectCount);
        HashMap<String, HashSet<String>> map = null;
        try {
            map = getHashMapByIdToIfcCSVParsing("resources//spec.csv", ",");
        } catch (Exception e) {
        }
        ;
        for (IfcElement element : elements) {

            try {
                if (!hasCorrctBsabId(element, map)) {

                    bsab96bwrongs.add(new Bsab96bdWrong(
                            element.getOid(),
                            getNameOfElement(getIfcBuildingFromElementOrNull(element)),
                            getNameOfElement(getIfcBuildingStoreyFromElementOrNull(element)),
                            extractNameFromClass(element.getClass()),
                            getBSABFromElement(element, map),
                            validIdsFromHashSetToString(map.get(extractNameFromClass(element.getClass()))),
                            getNameOfElement(getIfcSiteFromElementOrNull(element)),
                            revisionId,
                            element.getName()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ;
        try {
            repo.writeBsab96bdWrong(bsab96bwrongs);
        } catch (Exception e) {
            repo.writeLog(new Log(e.getMessage(), revisionId));
        }
    }

    public boolean hasBsabId(IfcElement element) {
        List<IfcPropertySet> propertySets = getIfcPropertySetsFromElementOrNull(element);
        IfcPropertySet propertySet = getPropertySetFromListByStartsWithOrNull(propertySets, "AH");
        return propertySet != null;

    }
    public boolean hasCorrctBsabId(IfcElement element, HashMap<String, HashSet<String>> map) {
        try {
            List<IfcPropertySet> propertySets = getIfcPropertySetsFromElement(element);
            IfcPropertySet propertySet = getPropertySetFromListByStartsWith(propertySets, "AH");
            String idToCheck = extractTextValueByNameOfSingleValue(propertySet, "BSAB96BD");
            String className = extractNameFromClass(element.getClass());
            return map.get(className).contains(idToCheck);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public String getBSABFromElement(IfcElement element, HashMap<String, HashSet<String>> map) {
        try {

            List<IfcPropertySet> propertySets = getIfcPropertySetsFromElement(element);
            IfcPropertySet propertySet = getPropertySetFromListByStartsWith(propertySets, "AH");
            String idToCheck = extractTextValueByNameOfSingleValue(propertySet, "BSAB96BD");
            return idToCheck;
        } catch (Exception e) {
            return null;
        }
    }

    public String validIdsFromHashSetToString(HashSet<String> set) {
        String string = "";
        int counter = 0;
        for (String element : set) {
            if (counter == 0) {
                string += element;
            } else {
                string += ", " + element;
            }
            counter++;
        }
        return string;
    }
}

