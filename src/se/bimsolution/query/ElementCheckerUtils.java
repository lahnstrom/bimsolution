package se.bimsolution.query;

import org.bimserver.models.ifc2x3tc1.IfcElement;
import org.bimserver.models.ifc2x3tc1.IfcPropertySet;
import se.bimsolution.db.Fail;
import se.bimsolution.db.IfcType;
import se.bimsolution.db.PropertySet;

import java.io.IOException;
import java.util.*;

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
/*    static TwoParameterElementChecker isBSABIdCorrect = (map, element) -> {
            List<IfcPropertySet> propertySets = getIfcPropertySetsFromElement(element);
            IfcPropertySet propertySet = getPropertySetFromListByStartsWith(propertySets, "AH");
            String idToCheck = extractTextValueByNameOfSingleValue(propertySet, "BSAB96BD");
            String className = extractNameFromClass(element.getClass());
            return map.get(className).contains(idToCheck);

    };*/

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

    /**
     * Given a collection of elements, returns a HashMap of IfcElements and corresponding newly created PropertySet.
     * If the element has no propertyset, the value will be null.
     *
     * @param elements A list of elements that can have AH property sets
     * @return A hashmap: IfcElement - new PropertySet(element)
     */
    public static HashMap<IfcElement, PropertySet> newIfcElementToAHPropertySetMap(Collection<IfcElement> elements) {
        HashMap<IfcElement, PropertySet> elementPropertySetHashMap = new HashMap<>();
        for (IfcElement element :
                elements) {
            try {
                IfcPropertySet pset = getPropertySetFromListByStartsWith(getIfcPropertySetsFromElement(element), "AH");
                elementPropertySetHashMap.put(element, newPropertySetFromAHIfcPropertySet(pset));
            } catch (Exception ignored) {
                elementPropertySetHashMap.put(element, null);
            }
        }
        return elementPropertySetHashMap;
    }

    /**
     * Given the following:
     * A revisionId, that is a DB revision id,
     * A HashMap with Elements relating IfcElements to the ID of their propertySet in the DB,
     * A HashMap with ifc_name (class name) relating to the ID of their corresponding IfcType in the DB,
     * A HashMap with ElementChecker lambdas and their corresponding Error code in the  DB,
     * <p>
     * This method returns a list of newly created failures ready to be written to DB.
     * The list contains all the failed checks after running all the ElementCheckers on all the Elements in the maps.
     *
     * @param revisionId          A DB revision id.
     * @param elementsPSetIdMap   HashMap of IfcElement - ID of PropertySet
     * @param ifcTypeNameToIdMap  HashMap of ClassName - ID of IfcType
     * @param callbacksErrorIdMap ElementChecker - ID of Error
     * @return A list of all the failed checks for the elements
     */
    public static List<Fail> checkAllElementsInHashMap(int revisionId,
                                                       HashMap<IfcElement, Integer> elementsPSetIdMap,
                                                       HashMap<String, Integer> ifcTypeNameToIdMap,
                                                       HashMap<ElementChecker, Integer> callbacksErrorIdMap) {
        List<Fail> fails = new ArrayList<>();
        for (IfcElement element :
                elementsPSetIdMap.keySet()) {
            int ifcTypeId = ifcTypeNameToIdMap.get(extractNameFromClass(element.getClass()));
            for (ElementChecker callback :
                    callbacksErrorIdMap.keySet()) {

                if (!callback.checkElement(element)) {
                    fails.add(newFailFromElement(element, callbacksErrorIdMap.get(callback),
                            revisionId, ifcTypeId, elementsPSetIdMap.get(element)));
                }
            }

        }
        return fails;
    }

    /**
     * Given an IfcElement that has failed a check, and arguments that describes the check,
     * returns a new Fail with names of IfcSite, building and storey added.
     *
     * @param element   An IfcElement that has failed a check
     * @param errorId   The error id of the failing check
     * @param roid      The revision id of the project being checked
     * @param ifcTypeId The ifcTypeId that is related to the element.
     * @param psetId    The id of the IfcPropertySet that relates to the element
     * @return A newly created fail ready to be written to the db.
     */
    public static Fail newFailFromElement(IfcElement element, int errorId, int roid, int ifcTypeId, int psetId) {
        long oid = element.getOid();
        String ifcSite = getIfcSiteFromElement(element).getName();
        String ifcBuilding = getIfcBuildingFromElement(element).getName();
        String ifcStorey = getIfcBuildingStoreyFromElement(element).getName();
        return new Fail(oid, roid, errorId, ifcTypeId, ifcSite, ifcBuilding, ifcStorey, psetId);
    }

    /**
     * Given an IfcClassName, a map with relations between such class names and multiple IDS,
     * and a Delimiter for separating the correct ids from each other,
     * returns a String with the multiple IDs.
     * The map can be generated with getHashMapByIdToIfcCSVParsing
     *
     * @param className       Name of an Ifc class
     * @param returnDelimiter The delimiter between the correct ids in the return string
     * @return A string with the correct ids in the class separated by returnDelimiter.
     */

    public static String getStringOfValidBSAB96BD(String className, HashMap<String, HashSet<String>> map, String returnDelimiter) {
        StringBuilder stringBuilder = new StringBuilder();
        System.out.println(className);
        for (String valid :
                map.get(className)) {
            stringBuilder.append(valid).append(returnDelimiter);
        }
        return stringBuilder.toString();
    }

    /**
     * Given a filepath to a csv file containing a mapping between IfcClasses and an Id,
     * this method returns a list of newly created IfcType instances, ready to be written to DB.
     *
     * @param filePath               The filepath to a csv file containing the mappings.
     * @param csvDelimiter           The csv file delimiter, usually a comma
     * @param validBSAB96BDDelimiter The desired delimiter between the different IDs in the BSAB96BD String
     *                               on the created instances
     * @return A list of newly created IfcType instances, ready to be written to DB.
     */
    public static List<IfcType> createIfcTypesFromStandardClassList(String filePath, String csvDelimiter, String validBSAB96BDDelimiter) {
        List<IfcType> typeList = new ArrayList<>();
        List<Class> classList = getStandardClassList();
        HashMap<String, HashSet<String>> map = null;
        try {
            map = getHashMapByIdToIfcCSVParsing(csvDelimiter, filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Class clazz :
                classList) {
            String className = extractNameFromClass(clazz);
            typeList.add(new IfcType(className, getStringOfValidBSAB96BD(className, map, validBSAB96BDDelimiter)));

        }
        return typeList;
    }

    /**
     * Given an IfcPropertySet, returns a new PropertySet object that corresponds to a database row.
     *
     * @param pset An IfcPropertySet from which to get the values.
     * @return A new PropertySet ready to be written to DB.
     */
    public static PropertySet newPropertySetFromAHIfcPropertySet(IfcPropertySet pset) {
        String benamning = extractTextValueByNameOfSingleValue(pset, "Benämning");
        String beteckning = extractTextValueByNameOfSingleValue(pset, "Beteckning");
        String typId = extractTextValueByNameOfSingleValue(pset, "TypID");
        String BSAB96BD = extractTextValueByNameOfSingleValue(pset, "BSAB96BD");
        return new PropertySet(benamning, beteckning, typId, BSAB96BD);
    }

    /**
     * Given an IfcPropertySet and a name, extracts the value of the IfcSingleValue with the name.
     * Wraps the two methods getNominalTextValueFromSingleValue and getSingleValueFromPropertySetByName and returns null if either
     * method throws an exception.
     *
     * @param propertySet An IfcPropertySet from which to extract a single value.
     * @param name        The name of the single value asked for
     * @return The wrapped value of the single value.
     */
    public static String extractTextValueByNameOfSingleValue(IfcPropertySet propertySet, String name) {
        String ret;
        try {
            ret = getNominalTextValueFromSingleValue(getSingleValueFromPropertySetByName(propertySet, name));
        } catch (Exception e) {
            ret = null;
        }
        return ret;
    }
}
