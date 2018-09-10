package se.bimsolution.query;

import org.bimserver.models.ifc2x3tc1.IfcPropertySet;

import java.util.HashMap;
import java.util.List;
import static se.bimsolution.query.QueryUtils.*;

public class ElementCheckerUtils {

    /**
     * This function checks if an ifc element has a property set.
     *
     * @param element   IfcElement to check.
     * @return boolean  True if the element has a property set, otherwise false.
     */
    static ElementChecker hasPropertySet = element -> {
        try {
            getIfcPropertySetsFromElement(element);
            return true;
        } catch (Exception e) {
            return false;
        }
    };

    /**
     * This function checks if an ifc element has a BSAB Id.
     *
     * @param element   IfcElement to check.
     * @return boolean  True if the element has a BSAB Id, otherwise false.
     */
    static ElementChecker hasBSABId = element -> {
        List<IfcPropertySet> propertySets = getIfcPropertySetsFromElement(element);
        try {
            getPropertySetFromListByStartsWith(propertySets, "AH");
            return true;
        } catch (Exception e) {
            return false;
        }
    };

    /**
     * This function checks if an ifc element has the correct BSAB Id given a map of valid Ids of each ifc element.
     *
     * @param element   IfcElement to check.
     * @param map       Map of valid Ids of each ifc element.
     * @return boolean  True if the element has a BSAB Id, otherwise false.
     */
    static TwoParameterElementChecker isBSABIdCorrect = (map, element) -> {
            List<IfcPropertySet> propertySets = getIfcPropertySetsFromElement(element);
            IfcPropertySet propertySet = getPropertySetFromListByStartsWith(propertySets, "AH");
            String idToCheck = extractTextValueByNameOfSingleValue(propertySet, "BSAB96BD");
            String className = extractNameFromClass(element.getClass());
            return map.get(className).contains(idToCheck);

    };

    /**
     * This function checks if an ifc element is in the storey it's associated with, i.e. if the element is above the
     * floor of its storey and below the floor of the storey above.
     *
     * @param element   Ifc element to check.
     * @return boolean  True if the element is on the correct floor, otherwise false.
     */
    static ElementChecker isObjectOnCorrectFloor = element -> {
        final double TOLERANCE = 0.1;
        double elementZvalue = -getHeightDifferenceBetweenStoreyAndElement(element);
        double storeyZvalue = getHeightOfStorey(getIfcBuildingStoreyFromElement(element));
        return !getElementIsBelowFloorLevel(element) && (elementZvalue - storeyZvalue) < TOLERANCE;
    };

    static ElementChecker isMasonary = element -> {

      return true;
    };

    /**
     * This method should return a HashMap of ElementChecker - ID as it is written in the DB.
     * Implementation might vary over time.
     * @return A HashMap of ElementChecker, ID
     */
    static HashMap<ElementChecker, Integer> standardElementCheckerMapping() {
        HashMap<ElementChecker, Integer> retMap = new HashMap<>();
        retMap.put(hasPropertySet, 1);
        retMap.put(hasBSABId, 2);
//        retMap.put(isObjectOnCorrectFloor, 3);
        return retMap;
    }
}
