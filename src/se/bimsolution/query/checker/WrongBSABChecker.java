package se.bimsolution.query.checker;

import org.bimserver.emf.IfcModelInterface;
import org.bimserver.models.ifc2x3tc1.IfcElement;
import org.bimserver.models.ifc2x3tc1.IfcPropertySet;

import static se.bimsolution.query.utils.QueryUtils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import se.bimsolution.db.object.Bsab96bdWrong;
import se.bimsolution.db.object.Log;
import se.bimsolution.db.repo.Repository;
import se.bimsolution.db.object.ObjectCount;

import static se.bimsolution.query.utils.ElementCheckerUtils.extractTextValueByNameOfSingleValue;

public class WrongBSABChecker extends ElementChecker {

    public WrongBSABChecker(IfcModelInterface model, Repository repo, int revisionId, List<Class<? extends IfcElement>> classList) {
        super(model, repo, revisionId, classList);
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

        repo.writeObjectCount(new ObjectCount(revisionId, extractNameFromClass(this.getClass()), elements.size()));
        HashMap<String, HashSet<String>> map = null;
        try {
            map = getHashMapByIdToIfcCSVParsing("resources//spec.csv", ",");
        } catch (Exception ignored) {
        }

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
            return map.get(className).contains(idToCheck) || map.get("Varierar").contains(idToCheck);
        } catch (IllegalArgumentException e) {
            return false;
        } catch (NullPointerException e) {
            System.out.println(extractNameFromClass(element.getClass()));
            e.printStackTrace();
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

