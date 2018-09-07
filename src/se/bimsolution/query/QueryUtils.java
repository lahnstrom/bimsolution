package se.bimsolution.query;

import org.bimserver.models.ifc2x3tc1.*;
import org.eclipse.emf.common.util.EList;
import se.bimsolution.db.Fail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Utility class for QueryMachines
 */
public final class QueryUtils {


    private QueryUtils() {
    }

    /**
     * The classes we're checking in the Query Machines
     *
     * @return a list of classes
     */
    public static List<Class> standardClassList() {
        List<Class> classList = new ArrayList<>();
        classList.add(IfcDoor.class);
        classList.add(IfcFlowSegment.class);
//        classList.add(IfcFurnitureType.class);
        classList.add(IfcAirTerminalType.class);
        classList.add(IfcAirToAirHeatRecoveryType.class);
        classList.add(IfcBeam.class);
        classList.add(IfcBuilding.class);
        classList.add(IfcCompressorType.class);
//        classList.add(IfcBuildingStorey.class);
        classList.add(IfcColumn.class);
        classList.add(IfcCovering.class);
        classList.add(IfcCurtainWall.class);
        classList.add(IfcAirTerminalType.class);
        classList.add(IfcAirTerminalBoxType.class);
        classList.add(IfcDuctSegmentType.class);
        classList.add(IfcDuctFittingType.class);
        classList.add(IfcDuctSilencerType.class);
        classList.add(IfcEquipmentElement.class);
        classList.add(IfcFireSuppressionTerminalType.class);
        classList.add(IfcFanType.class);
        classList.add(IfcFilterType.class);
        classList.add(IfcTankType.class);
        classList.add(IfcFlowTerminal.class);
        classList.add(IfcFooting.class);
        classList.add(IfcHumidifierType.class);
        classList.add(IfcUnitaryEquipmentType.class);
        classList.add(IfcPile.class);
        classList.add(IfcPumpType.class);
        classList.add(IfcPipeFittingType.class);
        classList.add(IfcSwitchingDeviceType.class);
        classList.add(IfcPipeSegmentType.class);
        classList.add(IfcRamp.class);
        classList.add(IfcRampFlight.class);
        classList.add(IfcStairFlight.class);
        classList.add(IfcStair.class);
        classList.add(IfcSlab.class);
        classList.add(IfcDamperType.class);
        classList.add(IfcRoof.class);
//        classList.add(IfcSite.class);
        classList.add(IfcJunctionBoxType.class);
//        classList.add(IfcSpace.class);
        classList.add(IfcOutletType.class);
        classList.add(IfcSystem.class);
        classList.add(IfcValveType.class);
        classList.add(IfcWall.class);
        classList.add(IfcFlowController.class);
        classList.add(IfcLightFixtureType.class);
        classList.add(IfcWindow.class);
//        classList.add(IfcOpeningElement.class);
        return classList;
    }

    /**
     * Given a .csv-file with two columns,
     * this method builds a HashMap where each key is a String representing an IfcObject
     * and where each value is a HashSet containing the correct IDs for the IfcObject, as strings.
     *
     * @param CSVfilepath A filepath to a csv file.
     * @param delimiter   The delimiter used in writing the CSVFile
     * @return A HashMap with IfcObject names mapped to their correctIDs
     * @throws IOException if file can not be found or read.
     */
    public static HashMap<String, HashSet<String>> idToIfcCSVParser(String CSVfilepath, String delimiter) throws IOException {
        String line;
        HashMap<String, HashSet<String>> parsedData = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(CSVfilepath));
        while ((line = br.readLine()) != null) {
            String str[] = line.split(delimiter);
            String key = str[1];
            String value = str[0];
            HashSet<String> valueList;

            if (parsedData.containsKey(key)) {
                valueList = parsedData.get(key);
            } else {
                valueList = new HashSet<>();
                parsedData.put(key.trim(), valueList);
            }
            valueList.add(value.trim());
        }
        return parsedData;
    }


    /**
     * Given an IfcElement, this method returns the IfcBuildingStorey within which the element is contained.
     *
     * @param element An IfcElement.
     * @return An IfcBuldingStorey within which the Element is contained.
     */
    public static IfcBuildingStorey ifcBuildingStoreyFromElement(IfcElement element) {
        EList<IfcRelContainedInSpatialStructure> relList = element.getContainedInStructure();
        for (IfcRelContainedInSpatialStructure rel :
                relList) {
            if (rel.getRelatingStructure() instanceof IfcBuildingStorey) {
                return (IfcBuildingStorey) rel.getRelatingStructure();
            }
        }
        throw new IllegalArgumentException("The element has no IfcBuildingStorey associated with it");
    }


    /**
     * Given an IfcElement, this method returns the IfcBuilding within which the element is contained.
     *
     * @param element An IfcElement.
     * @return An IfcBuilding within which the Element is contained.
     */
    public static IfcBuilding ifcBuildingFromElement(IfcElement element) {
        IfcBuildingStorey storey = ifcBuildingStoreyFromElement(element);
        EList<IfcRelDecomposes> decomposes = storey.getDecomposes();
        for (IfcRelDecomposes de : decomposes) {
            if (de.getRelatingObject() instanceof IfcBuilding) {
                return (IfcBuilding) de.getRelatingObject();
            }
        }
        throw new IllegalArgumentException("The element has no IfcBuilding associated with it");

    }


    /**
     * Given an IfcElement, this method returns the IfcSite within which the element is contained.
     *
     * @param element An IfcElement.
     * @return An IfcSite within which the Element is contained.
     */
    public static IfcSite ifcSiteFromElement(IfcElement element) {
        IfcBuilding building = ifcBuildingFromElement(element);
        EList<IfcRelDecomposes> decomposes = building.getDecomposes();
        for (IfcRelDecomposes de : decomposes) {
            if (de.getRelatingObject() instanceof IfcSite) {
                return (IfcSite) de.getRelatingObject();
            }
        }
        throw new IllegalArgumentException("The element has no IfcSite associated with it");
    }


    /**
     * Given an IfcObject, this method returns a list of the IfcPropertySets the element is defined by.
     *
     * @param element An IfcElement.
     * @return A list of IfcPropertySets which the element is defined by.
     */
    public static List<IfcPropertySet> ifcPropertySetsFromElement(IfcObject element) {
        List<IfcRelDefinesByProperties> definesList = getAllIfcRelDefinesByPropertiesFromObject(element);
        List<IfcPropertySet> psets = new ArrayList<>();
        for (IfcRelDefinesByProperties rel :
                definesList) {
            IfcPropertySetDefinition pset = rel.getRelatingPropertyDefinition();
            if (pset instanceof IfcPropertySet) {
                psets.add((IfcPropertySet) pset);
            }
        }
        if (psets.size() == 0) {
            throw new IllegalArgumentException("The element has no relating IfcPropertySet");
        }
        return psets;
    }

    /**
     * Given a list of IfcPropertySets and a string, this method returns the first PropertySet matching that string.
     *
     * @param propertySets A list of IfcPropertySets
     * @param name         A name of an IfcPropertySet
     * @return An IfcPropertySet
     */
    public static IfcPropertySet getPropertySetByName(List<IfcPropertySet> propertySets, String name) {
        for (IfcPropertySet pset :
                propertySets) {
            if (pset.getName().equals(name)) {
                return pset;
            }
        }
        throw new IllegalArgumentException("The property set with name " + name + " does not exist");
    }

    /**
     * Given a list of IfcPropertySets and a String,
     * this method returns the first PropertySet that starts with the string.
     *
     * @param propertySets A list of IfcPropertySets
     * @param startsWith   The first part of an IfcPropertySet name
     * @return An IfcPropertySet
     */
    public static IfcPropertySet getPropertySetByStartsWith(List<IfcPropertySet> propertySets, String startsWith) {
        for (IfcPropertySet pset :
                propertySets) {
            if (pset.getName().startsWith(startsWith)) {
                return pset;
            }
        }
        throw new IllegalArgumentException("No property starting with " + startsWith + " exist");
    }


    /**
     * Given an IfcPropertySet and a name, returns the singleValueWith that name.
     *
     * @param ifcPropertySet An IfcProperty that contains an IfcPropertySingleValue
     * @param name           The name of the property.
     * @return An IfcPropertySingleValue which has the name specified.
     */
    public static IfcPropertySingleValue getSingleValueByName(IfcPropertySet ifcPropertySet, String name) {
        EList<IfcProperty> properties = ifcPropertySet.getHasProperties();
        for (IfcProperty prop :
                properties) {
            if (prop instanceof IfcPropertySingleValue && prop.getName().equals(name)) {
                return (IfcPropertySingleValue) prop;
            }
        }
        throw new IllegalArgumentException("No IfcProperty found in the IfcPropertySet with the name: " + name);
    }

    /**
     * Given an IfcPropertySingleValue that has an IfcText nominal value,
     * this method returns the wrapped value as a string.
     *
     * @param singleValue an IfcPropertySingleValue.
     * @return the wrapped IfcTextValue as a String
     */
    public static String getNominalTextValueFromSingleValue(IfcPropertySingleValue singleValue) {
        IfcValue value = singleValue.getNominalValue();
        //Ta ut textvärdet om det finns
        if (value instanceof IfcText) {
            return ((IfcText) value).getWrappedValue();
        }
        throw new IllegalArgumentException("The IfcPropertySingleValue does not have a nominal value with type text");
    }

    /**
     * Given an IfcPropertySet and the name of a property,
     * Returns true if the property with the given name exists in the propertySet.
     *
     * @param propertySet  An IfcPropertySet
     * @param propertyName The name of the desired property
     * @return Does the property with the desired name exist?
     */
    public static boolean propertyExistsInPropertySet(IfcPropertySet propertySet, String propertyName) {
        EList<IfcProperty> properties = propertySet.getHasProperties();
        for (IfcProperty prop :
                properties) {
            if (prop.getName().equals(propertyName)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Given an IfcLocalPlacement which holds an IfcAxis2Placement3D as relative placement, return it.
     *
     * @param placement an IfcLocalPlacement
     * @return The IfcAxis2Placement
     */
    public static IfcAxis2Placement3D relatingIfcAxis3DFromLocalPlacement(IfcLocalPlacement placement) {
        if (placement.getRelativePlacement() instanceof IfcAxis2Placement3D) {
            return (IfcAxis2Placement3D) placement.getRelativePlacement();
        }
        throw new IllegalArgumentException("The placement does not have a relating IfcAxis2Placement3D object");
    }

    /**
     * Given an IfcAxis2Placement3D, returns the list of coordinates for the cartesian point relating to the placement.
     *
     * @param placement An IfcPlacement
     * @return The relative coordinates of the placement
     */
    public static List<Double> getRelatingCoordinatesOfPlacement(IfcAxis2Placement3D placement) {
        IfcCartesianPoint location = placement.getLocation();
        return location.getCoordinates();
    }

    /**
     * Takes a list of coordinates and returns the Z-value, assuming order x, y, z;
     *
     * @param coordinates A list of coordinates
     * @return The third element in the list;
     */
    public static double getZValueOfCoordinates(List<Double> coordinates) {
        return coordinates.get(2);
    }

    /**
     * Given a placement, returns the z-value of the relating cartesian point.
     *
     * @param placement an IfcPlacement
     * @return the z-value as a double.
     */
    public static double getZValueOfPlacement(IfcAxis2Placement3D placement) {
        return getZValueOfCoordinates(getRelatingCoordinatesOfPlacement(placement));
    }


    /**
     * Given an IfcProduct which has a relating IfcLocalPlacement, return that placement.
     *
     * @param product An IfcProduct with a local placement
     * @return The placement casted to an IfcLocalPlacement
     */
    public static IfcLocalPlacement getLocalPlacement(IfcProduct product) {
        if (product.getObjectPlacement() instanceof IfcLocalPlacement) {
            return (IfcLocalPlacement) product.getObjectPlacement();
        }
        throw new IllegalArgumentException("The product does not have a relating IfcLocalPlacement");
    }

    /**
     * Given an IfcLocalPlacement, returns its PlacementRelToLocalPlacement {
     */
    public static IfcLocalPlacement getLocalPlacement(IfcLocalPlacement placement) {
        if (placement.getPlacementRelTo() instanceof IfcLocalPlacement) {
            return (IfcLocalPlacement) placement.getPlacementRelTo();
        }
        throw new IllegalArgumentException("The placement does not have a relating IfcLocalPlacement");
    }


    /**
     * Given an IfcObjectPlacement, returns true if the placement places an IfcSite.
     *
     * @param placement An IfcObjectPlacement
     * @return Does the IfcObjectPlacement place an IfcSite?
     */
    public static boolean placementPlacesIfcSite(IfcObjectPlacement placement) {
        for (IfcProduct prod :
                placement.getPlacesObject()) {
            if (prod instanceof IfcSite) {
                return true;
            }
        }
        return false;
    }

    /**
     * Given an IfcProduct, steps through the object tree and sums the Z-vals in each step.
     * Returns the absolute z-value as it relates to the IfcSite
     *
     * @param product An IfcProduct
     * @return The absolute z-value of the product as a double
     */
    public static double getAbsoluteZValue(IfcProduct product) {
        double absoluteZValue = 0;
        IfcLocalPlacement localPlacement = getLocalPlacement(product);
        IfcAxis2Placement3D placement3D = relatingIfcAxis3DFromLocalPlacement(localPlacement);
        absoluteZValue += getZValueOfPlacement(placement3D);
        while (!placementPlacesIfcSite(localPlacement)) {
            localPlacement = getLocalPlacement(localPlacement);
            placement3D = relatingIfcAxis3DFromLocalPlacement(localPlacement);
            absoluteZValue += getZValueOfPlacement(placement3D);
        }
        return absoluteZValue;
    }

    /**
     * Given an IfcElement, returns true if the absolute Z value is below the Z value of its corresponding floor.
     *
     * @param element An IfcElement to check
     * @return Is the Z value of the Element below that of its floor?
     */
    public static boolean elementIsBelowFloorLevel(IfcElement element) {
        double storeyZ = getAbsoluteZValue(ifcBuildingStoreyFromElement(element));
        double elementZ = getAbsoluteZValue(element);
        return storeyZ > elementZ;
    }

    /**
     * Given an IfcElement and a threshold,
     * returns true if the absolute Z value is below the Z value of its corresponding floor
     * and if the difference is larger than the threshold.
     *
     * @param element An IfcElement to check
     * @return Is the Z value of the Element below that of its floor?
     */
    public static boolean elementIsBelowFloorLevel(IfcElement element, double threshold) {
        double storeyZ = getAbsoluteZValue(ifcBuildingStoreyFromElement(element));
        double elementZ = getAbsoluteZValue(element);
        return storeyZ - elementZ > threshold;
    }

    /**
     * Gets the height difference between storey and element.
     *
     * @param element An IfcElement to check against a storey
     * @return The height difference, e.g. StoreyHeight - ElementHeight
     */
    public static double getHeightDifferenceBetweenStoreyAndElement(IfcElement element) {
        return getAbsoluteZValue(ifcBuildingStoreyFromElement(element)) - getAbsoluteZValue(element);
    }

    /**
     * Given an IfcObject, return the list of all IfcRelDefines that are of subtype IfcRelDefinesByProperties.
     * @param object An IfcObject.
     * @return The IfcRelDefinesByProperties associated with the object.
     */
    public static List<IfcRelDefinesByProperties> getAllIfcRelDefinesByPropertiesFromObject(IfcObject object) {
        EList<IfcRelDefines> ifcRelDefinesEList = object.getIsDefinedBy();
        List<IfcRelDefinesByProperties> propertiesList = new ArrayList<>();
        for (IfcRelDefines rel :
                ifcRelDefinesEList) {
            if (rel instanceof IfcRelDefinesByProperties) {
                propertiesList.add((IfcRelDefinesByProperties) rel);
            }
        }
        return propertiesList;
    }

    /**
     * Given a collection of IfcElements and a list of ElementCheckers,
     * returns a list of all the failed checks when running all the ElementCheckers checkElement function
     * on all the elements in the collection.
     * @param elements A collection of IfcElements which to check.
     * @param callbacks The ElementCheckers to check with.
     * @return A list of all the failed checks for the elements
     */
    public static List<Fail> checkAllElementsInCollection(Collection<IfcElement> elements, ElementChecker... callbacks) {
        List<Fail> fails = new ArrayList<>();
        for (IfcElement element:
             elements) {
            for (ElementChecker callback:
                 callbacks) {
                if (!callback.checkElement(element)) {
                    //TODO Implement this.
                }
            }

        }
        return fails;
    }


//    static void test() {
//        checkAllElementsInCollection(List<Elements>, ElementCheckerUtil.checker1, Elementc)
//    }
//
//     ElementChecker wrongFloorChecker = (element) -> {
//        if element.onWrongFloor return false;
//        else return true;
//    };



    /**
     * Given an IfcObject, this method returns a list of the IfcPropertySets the element is defined by.
     *
     * @param object An IfcObject.
     * @return A list of IfcPropertySets which the element is defined by.
     */
    public static List<IfcElementQuantity> ifcElementQuantitiesFromObject(IfcObject object) {
        List<IfcRelDefinesByProperties> definesList = getAllIfcRelDefinesByPropertiesFromObject(object);
        List<IfcElementQuantity> elementQuantities = new ArrayList<>();
        for (IfcRelDefinesByProperties rel :
                definesList) {
            IfcPropertySetDefinition pset = rel.getRelatingPropertyDefinition();
            if (pset instanceof IfcElementQuantity) {
                elementQuantities.add((IfcElementQuantity) pset);
            }
        }
        if (elementQuantities.size() == 0) {
            throw new IllegalArgumentException("The element has no relating IfcElementQuantity");
        }
        return elementQuantities;
    }


}
