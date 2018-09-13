package se.bimsolution.query;

import org.bimserver.models.ifc2x3tc1.*;
import org.eclipse.emf.common.util.EList;

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
    public static List<Class> getStandardClassList() {
        List<Class> classList = new ArrayList<>();
        classList.add(IfcDoor.class);
//        classList.add(IfcFlowSegment.class);
//        classList.add(IfcFurnitureType.class);
//        classList.add(IfcAirTerminalType.class);
//        classList.add(IfcAirToAirHeatRecoveryType.class);
        classList.add(IfcBeam.class);
//        classList.add(IfcBuilding.class);
        classList.add(IfcCompressorType.class);
//        classList.add(IfcBuildingStorey.class);
        classList.add(IfcColumn.class);
        classList.add(IfcCovering.class);
        classList.add(IfcCurtainWall.class);
//        classList.add(IfcAirTerminalType.class);
        classList.add(IfcAirTerminalBoxType.class);
        classList.add(IfcDuctSegmentType.class);
        classList.add(IfcDuctFittingType.class);
        classList.add(IfcDuctSilencerType.class);
        classList.add(IfcEquipmentElement.class);
//        classList.add(IfcFireSuppressionTerminalType.class);
        classList.add(IfcFanType.class);
//        classList.add(IfcFilterType.class);
        classList.add(IfcTankType.class);
        classList.add(IfcFlowTerminal.class);
        classList.add(IfcFooting.class);
//        classList.add(IfcHumidifierType.class);
        classList.add(IfcUnitaryEquipmentType.class);
        classList.add(IfcPile.class);
        classList.add(IfcPumpType.class);
//        classList.add(IfcPipeFittingType.class);
        classList.add(IfcSwitchingDeviceType.class);
//        classList.add(IfcPipeSegmentType.class);
        classList.add(IfcRamp.class);
        classList.add(IfcRampFlight.class);
        classList.add(IfcStairFlight.class);
        classList.add(IfcStair.class);
        classList.add(IfcSlab.class);
//        classList.add(IfcDamperType.class);
        classList.add(IfcRoof.class);
//        classList.add(IfcSite.class);
//        classList.add(IfcJunctionBoxType.class);
//        classList.add(IfcSpace.class);
        classList.add(IfcOutletType.class);
        classList.add(IfcSystem.class);
//        classList.add(IfcValveType.class);
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
    public static HashMap<String, HashSet<String>> getHashMapByIdToIfcCSVParsing(String CSVfilepath, String delimiter) throws IOException {
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
    public static IfcBuildingStorey getIfcBuildingStoreyFromElement(IfcElement element) {
        EList<IfcRelContainedInSpatialStructure> relList = element.getContainedInStructure();
        for (IfcRelContainedInSpatialStructure rel :
                relList) {
            if (rel.getRelatingStructure() instanceof IfcBuildingStorey) {
                return (IfcBuildingStorey) rel.getRelatingStructure();
            }
            if (rel.getRelatingStructure() instanceof IfcSpace) {
                IfcSpace space = (IfcSpace) rel.getRelatingStructure();
                for (IfcRelDecomposes dec : space.getDecomposes()) {
                    if (dec.getRelatingObject() instanceof IfcBuildingStorey) {
                        return (IfcBuildingStorey) dec.getRelatingObject();
                    }
                }
            }
        }

        throw new IllegalArgumentException("The element has no IfcBuildingStorey associated with it "
                + extractNameFromClass(element.getClass()));
    }

    /**
     * @param space
     * @return
     */
    public static IfcBuildingStorey getIfcStoreyFromIfcSpaceOrNull(IfcSpace space) {
        for (IfcRelDecomposes agg :
                space.getDecomposes()) {
            if (agg.getRelatingObject() instanceof IfcBuildingStorey) {
                return (IfcBuildingStorey) agg.getRelatingObject();
            }
        }
        return null;
    }

    /**
     * @param space
     * @return
     */
    public static IfcBuilding getIfcBuildingFromIfcSpaceOrNull(IfcSpace space) {
        IfcBuildingStorey storey = getIfcStoreyFromIfcSpaceOrNull(space);
        if (storey != null) {
            for (IfcRelDecomposes agg : storey.getDecomposes()) {
                if (agg.getRelatingObject() instanceof IfcBuilding) {
                    return (IfcBuilding) agg.getRelatingObject();
                }
            }
        }
        return null;
    }

    /**
     * @param space
     * @return
     */
    public static IfcSite getIfcSiteFromIfcSpaceOrNull(IfcSpace space) {
        IfcBuilding building = getIfcBuildingFromIfcSpaceOrNull(space);
        if (building != null) {
            for (IfcRelDecomposes agg : building.getDecomposes()) {
                if (agg.getRelatingObject() instanceof IfcSite) {
                    return (IfcSite) agg.getRelatingObject();
                }
            }
        }
        return null;
    }

    public static String getNameOfObjectOrNull(IfcRoot root) {
        try {
            return root.getName();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Given an IfcElement, this method returns the IfcBuildingStorey within which the element is contained.
     * If no such storey exists, returns null;
     *
     * @param element An IfcElement.
     * @return An IfcBuldingStorey within which the Element is contained.
     */
    public static IfcBuildingStorey getIfcBuildingStoreyFromElementOrNull(IfcElement element) {
        try {
            return getIfcBuildingStoreyFromElement(element);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Given an IfcElement, this method returns the IfcBuilding within which the element is contained.
     *
     * @param element An IfcElement.
     * @return An IfcBuilding within which the Element is contained.
     */
    public static IfcBuilding getIfcBuildingFromElement(IfcElement element) {
        IfcBuildingStorey storey = getIfcBuildingStoreyFromElement(element);
        EList<IfcRelDecomposes> decomposes = storey.getDecomposes();
        for (IfcRelDecomposes de : decomposes) {
            if (de.getRelatingObject() instanceof IfcBuilding) {
                return (IfcBuilding) de.getRelatingObject();
            }
        }
        throw new IllegalArgumentException("The element has no IfcBuilding associated with it");

    }

    public static boolean getPropertySetExistsWithNameStartsWith(String name, IfcElement element) {
        try {
            List<IfcPropertySet> propertySets = getIfcPropertySetsFromElement(element);
            for (IfcPropertySet pset :
                    propertySets) {
                if (pset.getName().startsWith(name)) {
                    return true;
                }
            }
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }


    /**
     * Given an IfcElement, this method returns the IfcBuilding within which the element is contained.
     * If no such building exists, returns null;
     *
     * @param element An IfcElement.
     * @return An IfcBuilding within which the Element is contained.
     */
    public static IfcBuilding getIfcBuildingFromElementOrNull(IfcElement element) {
        try {
            IfcBuildingStorey storey = getIfcBuildingStoreyFromElement(element);

            EList<IfcRelDecomposes> decomposes = storey.getDecomposes();
            for (IfcRelDecomposes de : decomposes) {
                if (de.getRelatingObject() instanceof IfcBuilding) {
                    return (IfcBuilding) de.getRelatingObject();
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Given an IfcElement, this method returns the IfcSite within which the element is contained.
     *
     * @param element An IfcElement.
     * @return An IfcSite within which the Element is contained.
     */
    public static IfcSite getIfcSiteFromElement(IfcElement element) {
        IfcBuilding building = getIfcBuildingFromElement(element);
        EList<IfcRelDecomposes> decomposes = building.getDecomposes();
        for (IfcRelDecomposes de : decomposes) {
            if (de.getRelatingObject() instanceof IfcSite) {
                return (IfcSite) de.getRelatingObject();
            }
        }
        throw new IllegalArgumentException("The element has no IfcSite associated with it");
    }


    /**
     * Given an IfcElement, this method returns the IfcSite within which the element is contained.
     * If no site exists for the element, returns null;
     *
     * @param element An IfcElement.
     * @return An IfcSite within which the Element is contained.
     */
    public static IfcSite getIfcSiteFromElementOrNull(IfcElement element) {
        try {
            IfcBuilding building = getIfcBuildingFromElement(element);
            EList<IfcRelDecomposes> decomposes = building.getDecomposes();
            for (IfcRelDecomposes de : decomposes) {
                if (de.getRelatingObject() instanceof IfcSite) {
                    return (IfcSite) de.getRelatingObject();
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Given an IfcObject, this method returns a list of the IfcPropertySets the element is defined by.
     *
     * @param object An IfcElement.
     * @return A list of IfcPropertySets which the element is defined by.
     */
    public static List<IfcPropertySet> getIfcPropertySetsFromElement(IfcObject object) {
        List<IfcRelDefinesByProperties> definesList = getAllIfcRelDefinesByPropertiesFromObject(object);
        List<IfcPropertySet> psets = new ArrayList<>();
        for (IfcRelDefinesByProperties rel :
                definesList) {
            IfcPropertySetDefinition pset = rel.getRelatingPropertyDefinition();
            if (pset instanceof IfcPropertySet) {
                psets.add((IfcPropertySet) pset);
            }
        }
        if (psets.size() == 0) {
            throw new IllegalArgumentException("The element has no relating IfcPropertySet " + extractNameFromClass(object.getClass()));
        }
        return psets;
    }

    /**
     * Given an IfcObject, this method returns a list of the IfcPropertySets the element is defined by.
     * If no such psets exist, returns null;
     *
     * @param object An IfcElement.
     * @return A list of IfcPropertySets which the element is defined by.
     */
    public static List<IfcPropertySet> getIfcPropertySetsFromElementOrNull(IfcObject object) {
        List<IfcRelDefinesByProperties> definesList = getAllIfcRelDefinesByPropertiesFromObject(object);
        List<IfcPropertySet> psets = new ArrayList<>();
        for (IfcRelDefinesByProperties rel :
                definesList) {
            IfcPropertySetDefinition pset = rel.getRelatingPropertyDefinition();
            if (pset instanceof IfcPropertySet) {
                psets.add((IfcPropertySet) pset);
            }
        }
        if (psets.size() == 0) {
            return null;
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
    public static IfcPropertySet getPropertySetFromListByName(List<IfcPropertySet> propertySets, String name) {
        for (IfcPropertySet pset :
                propertySets) {
            if (pset.getName().equals(name)) {
                return pset;
            }
        }
        throw new IllegalArgumentException("The property set with name " + name + " does not exist");
    }

    /**
     * Given a list of IfcPropertySets and a string, this method returns the first PropertySet matching that string.
     * If no PropertySet matches, returns null;
     *
     * @param propertySets A list of IfcPropertySets
     * @param name         A name of an IfcPropertySet
     * @return An IfcPropertySet
     */
    public static IfcPropertySet getPropertySetFromListByNameOrNull(List<IfcPropertySet> propertySets, String name) {
        for (IfcPropertySet pset :
                propertySets) {
            if (pset.getName().equals(name)) {
                return pset;
            }
        }
        return null;
    }


    /**
     * Given a list of IfcPropertySets and a String,
     * this method returns the first PropertySet that starts with the string.
     *
     * @param propertySets A list of IfcPropertySets
     * @param startsWith   The first part of an IfcPropertySet name
     * @return An IfcPropertySet
     */
    public static IfcPropertySet getPropertySetFromListByStartsWith(List<IfcPropertySet> propertySets, String startsWith) {
        for (IfcPropertySet pset :
                propertySets) {
            if (pset.getName().startsWith(startsWith)) {
                return pset;
            }
        }
        throw new IllegalArgumentException("No property starting with " + startsWith + " exist");
    }

    /**
     * Given a list of IfcPropertySets and a String,
     * this method returns the first PropertySet that starts with the string.
     * If no psets match, return null;
     *
     * @param propertySets A list of IfcPropertySets
     * @param startsWith   The first part of an IfcPropertySet name
     * @return An IfcPropertySet
     */
    public static IfcPropertySet getPropertySetFromListByStartsWithOrNull(List<IfcPropertySet> propertySets, String startsWith) {
        for (IfcPropertySet pset :
                propertySets) {
            if (pset.getName().startsWith(startsWith)) {
                return pset;
            }
        }
        return null;
    }

    /**
     * Given an IfcPropertySet and a name, returns the singleValueWith that name.
     *
     * @param ifcPropertySet An IfcProperty that contains an IfcPropertySingleValue
     * @param name           The name of the property.
     * @return An IfcPropertySingleValue which has the name specified.
     */
    public static IfcPropertySingleValue getSingleValueFromPropertySetByName(IfcPropertySet ifcPropertySet, String name) {
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
     * Given an IfcPropertySet and a name, returns the singleValue With that name.
     * If no such Single Value exists, return null;
     *
     * @param ifcPropertySet An IfcProperty that contains an IfcPropertySingleValue
     * @param name           The name of the property.
     * @return An IfcPropertySingleValue which has the name specified.
     */
    public static IfcPropertySingleValue getSingleValueFromPropertySetByNameOrNull(IfcPropertySet ifcPropertySet, String name) {
        EList<IfcProperty> properties = ifcPropertySet.getHasProperties();
        for (IfcProperty prop :
                properties) {
            if (prop instanceof IfcPropertySingleValue && prop.getName().equals(name)) {
                return (IfcPropertySingleValue) prop;
            }
        }
        return null;
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
     * Given an IfcPropertySingleValue that has an IfcText nominal value,
     * this method returns the wrapped value as a string.
     * If no textvalue exists, return null.
     *
     * @param singleValue an IfcPropertySingleValue.
     * @return the wrapped IfcTextValue as a String
     */
    public static String getNominalTextValueFromSingleValueOrNull(IfcPropertySingleValue singleValue) {
        IfcValue value = singleValue.getNominalValue();
        //Ta ut textvärdet om det finns
        if (value instanceof IfcText) {
            return ((IfcText) value).getWrappedValue();
        }
        return null;
    }


    /**
     * Constructs a HashMap of IfcBuilding Storey and the height.
     *
     * @param storeys IfcBuildingStorey
     * @return A new HashMap of IfcBuildingStorey - Height
     */
    public static HashMap<IfcBuildingStorey, Double> getHashMapOfStoreyAndHeight(Collection<IfcBuildingStorey> storeys) {
        HashMap<IfcBuildingStorey, Double> map = new HashMap<>();
        storeys.forEach(x -> map.put(x, getAbsoluteZValueFromProduct(x)));
        return map;
    }

    public static boolean getElementIsOnWrongStorey(IfcElement element, Collection<IfcBuildingStorey> storeys,
                                                    double threshold) {

        return getElementIsBelowFloorLevel(element, threshold) ||
                getElementIsAboveFloorLevel(element, threshold, storeys);
    }

    public static boolean getElementIsAboveFloorLevel(IfcElement element, double threshold, Collection<IfcBuildingStorey> storeys) {
        try {

            IfcBuildingStorey storey = getIfcBuildingStoreyFromElement(element);
            double storeyZ = getAbsoluteZValueFromProduct(storey);
            double elementZ = getAbsoluteZValueFromProduct(element);
            double floorHeight = getHeightDifferenceToNextStoreyAboveThreshold(storeys, storey, threshold);
            return elementZ - floorHeight > storeyZ + threshold;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Given a collection of storeys and a storey in that collection, returns the height difference between the floor
     * above and the reference floor, given that the difference is larger than the threshold.
     *
     * @param storeys   A collection of IfcBuildingStoreys
     * @param storey    A specific Storey within that collection
     * @param threshold The minimum allowed height difference to the next floor
     * @return The height difference to the first floor above threshold.
     */
    public static double getHeightDifferenceToNextStoreyAboveThreshold(Collection<IfcBuildingStorey> storeys,
                                                                       IfcBuildingStorey storey,
                                                                       double threshold) {
        HashMap<IfcBuildingStorey, Double> map = getHashMapOfStoreyAndHeight(storeys);
        double minValueAboveThreshold = Double.MAX_VALUE;
        double refHeight = getAbsoluteZValueFromProduct(storey);
        for (double height :
                map.values()) {
            if (height > refHeight + threshold && height < minValueAboveThreshold) {
                minValueAboveThreshold = height;
            }
        }
        if (minValueAboveThreshold == Double.MAX_VALUE) {
            return 0;
        }
        return minValueAboveThreshold - refHeight;
    }

    /**
     * Given an IfcPropertySet and the name of a property,
     * Returns true if the property with the given name exists in the propertySet.
     *
     * @param propertySet  An IfcPropertySet
     * @param propertyName The name of the desired property
     * @return Does the property with the desired name exist?
     */
    public static boolean getPropertyExistsInPropertySetByName(IfcPropertySet propertySet, String propertyName) {
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
    public static IfcAxis2Placement3D getRelatingIfcAxis3DFromLocalPlacement(IfcLocalPlacement placement) {
        if (placement.getRelativePlacement() instanceof IfcAxis2Placement3D) {
            return (IfcAxis2Placement3D) placement.getRelativePlacement();
        }
        throw new IllegalArgumentException("The placement does not have a relating IfcAxis2Placement3D object");
    }

    /**
     * Given an IfcLocalPlacement which holds an IfcAxis2Placement3D as relative placement, return it.
     * If it doesn't exist, return null;
     *
     * @param placement an IfcLocalPlacement
     * @return The IfcAxis2Placement
     */
    public static IfcAxis2Placement3D getRelatingIfcAxis3DFromLocalPlacementOrNull(IfcLocalPlacement placement) {
        if (placement.getRelativePlacement() instanceof IfcAxis2Placement3D) {
            return (IfcAxis2Placement3D) placement.getRelativePlacement();
        }
        return null;
    }

    /**
     * Given an IfcAxis2Placement3D, returns the list of coordinates for the cartesian point relating to the placement.
     *
     * @param placement An IfcPlacement
     * @return The relative coordinates of the placement
     */
    public static List<Double> getRelatingCoordinatesFromAxis2Placement(IfcAxis2Placement3D placement) {
        IfcCartesianPoint location = placement.getLocation();
        return location.getCoordinates();
    }

    /**
     * Takes a list of coordinates and returns the Z-value, assuming order x, y, z;
     *
     * @param coordinates A list of coordinates
     * @return The third element in the list;
     */
    public static double getZValueOfCoordinatesFromList(List<Double> coordinates) {
        return coordinates.get(2);
    }

    /**
     * Given a placement, returns the z-value of the relating cartesian point.
     *
     * @param placement an IfcPlacement
     * @return the z-value as a double.
     */
    public static double getZValueOfPlacementFromPlacement(IfcAxis2Placement3D placement) {
        return getZValueOfCoordinatesFromList(getRelatingCoordinatesFromAxis2Placement(placement));
    }


    /**
     * Given an IfcProduct which has a relating IfcLocalPlacement, return that placement.
     *
     * @param product An IfcProduct with a local placement
     * @return The placement casted to an IfcLocalPlacement
     */
    public static IfcLocalPlacement getLocalPlacementFromProduct(IfcProduct product) {
        if (product.getObjectPlacement() instanceof IfcLocalPlacement) {
            return (IfcLocalPlacement) product.getObjectPlacement();
        }
        throw new IllegalArgumentException("The product does not have a relating IfcLocalPlacement");
    }

    /**
     * Given an IfcProduct which has a relating IfcLocalPlacement, return that placement.
     * If it doesn't exist, return null.
     *
     * @param product An IfcProduct with a local placement
     * @return The placement casted to an IfcLocalPlacement
     */
    public static IfcLocalPlacement getLocalPlacementFromProductOrNull(IfcProduct product) {
        if (product.getObjectPlacement() instanceof IfcLocalPlacement) {
            return (IfcLocalPlacement) product.getObjectPlacement();
        }
        return null;
    }

    /**
     * Given an IfcLocalPlacement, returns its PlacementRelToLocalPlacement {
     */
    public static IfcLocalPlacement getLocalPlacementFromPlacement(IfcLocalPlacement placement) {
        if (placement.getPlacementRelTo() instanceof IfcLocalPlacement) {
            return (IfcLocalPlacement) placement.getPlacementRelTo();
        }
        throw new IllegalArgumentException("The placement does not have a relating IfcLocalPlacement");
    }


    /**
     * Given an IfcLocalPlacement, returns its PlacementRelToLocalPlacement {
     * If it doesn't exist, return null;
     */
    public static IfcLocalPlacement getLocalPlacementFromPlacementOrNull(IfcLocalPlacement placement) {
        if (placement.getPlacementRelTo() instanceof IfcLocalPlacement) {
            return (IfcLocalPlacement) placement.getPlacementRelTo();
        }
        return null;
    }


    /**
     * Given an IfcObjectPlacement, returns true if the placement places an IfcSite.
     *
     * @param placement An IfcObjectPlacement
     * @return Does the IfcObjectPlacement place an IfcSite?
     */
    public static boolean getPlacementPlacesIfcSite(IfcObjectPlacement placement) {
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
    public static double getAbsoluteZValueFromProduct(IfcProduct product) {
        double absoluteZValue = 0;
        IfcLocalPlacement localPlacement = getLocalPlacementFromProduct(product);
        IfcAxis2Placement3D placement3D = getRelatingIfcAxis3DFromLocalPlacement(localPlacement);
        absoluteZValue += getZValueOfPlacementFromPlacement(placement3D);
        while (!getPlacementPlacesIfcSite(localPlacement)) {
            localPlacement = getLocalPlacementFromPlacement(localPlacement);
            placement3D = getRelatingIfcAxis3DFromLocalPlacement(localPlacement);
            absoluteZValue += getZValueOfPlacementFromPlacement(placement3D);
        }
        return absoluteZValue;
    }

    /**
     * Given an IfcElement, returns true if the absolute Z value is below the Z value of its corresponding floor.
     *
     * @param element An IfcElement to check
     * @return Is the Z value of the Element below that of its floor?
     */
    public static boolean getElementIsBelowFloorLevel(IfcElement element) {
        double storeyZ = getAbsoluteZValueFromProduct(getIfcBuildingStoreyFromElement(element));
        double elementZ = getAbsoluteZValueFromProduct(element);
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
    public static boolean getElementIsBelowFloorLevel(IfcElement element, double threshold) {
        try {
            double storeyZ = getAbsoluteZValueFromProduct(getIfcBuildingStoreyFromElement(element));
            double elementZ = getAbsoluteZValueFromProduct(element);
            return storeyZ - elementZ > threshold;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Gets the height difference between storey and element.
     *
     * @param element An IfcElement to check against a storey
     * @return The height difference, e.g. StoreyHeight - ElementHeight
     */
    public static double getHeightDifferenceBetweenStoreyAndElement(IfcElement element) {
        return getAbsoluteZValueFromProduct(getIfcBuildingStoreyFromElement(element)) - getAbsoluteZValueFromProduct(element);
    }

    /**
     * Given an IfcObject, return the list of all IfcRelDefines that are of subtype IfcRelDefinesByProperties.
     *
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
     * Takes a name of an IfcClass and extracts the name e.g.
     * class org.bimserver.models.ifc2x3tc1.IfcDoor turns into string IfcDoor
     *
     * @param clazz a java class
     * @return a string, the name of the Ifc class.
     */

    public static String extractNameFromClass(Class<?> clazz) {
        String[] parts = clazz.toString().split("\\.");
        String ret = parts[parts.length - 1];
        if (ret.endsWith("Impl")) {
            ret = ret.substring(0, ret.length() - "Impl".length());
        }
        if (ret.endsWith("Type")) {
            ret = ret.substring(0, ret.length() - "Type".length());
        }
        if (ret.endsWith("Element")) {
            ret = ret.substring(0, ret.length() - "Element".length());
        }

        return ret;
    }


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


    /**
     * Given a name and a collection of IfcElementQuantities, returns the first that has the specified name
     * E.g. BaseQuantities
     *
     * @param quantities A collection of IfcElementQuantities
     * @param name       A name of the Specific quantity
     * @return A single IfcElementQuantity
     */
    public static IfcElementQuantity getElementQuantityFromCollectionByName(Collection<IfcElementQuantity> quantities, String name) {
        for (IfcElementQuantity quantity :
                quantities) {
            if (quantity.getName().equals(name)) {
                return quantity;
            }
        }
        throw new IllegalArgumentException("Quantity with name " + name + " does not exist on this object");
    }

    /**
     * See getElementQuantityFromCollectionByName, this method simply returns null when no fitting quantity is found
     *
     * @param quantities A collection of IfcElementQuantities
     * @param name       A name of the Specific quantity
     * @return A single IfcElementQuantity
     */
    public static IfcElementQuantity getElementQuantityFromCollectionByNameOrNull(
            Collection<IfcElementQuantity> quantities, String name) {
        for (IfcElementQuantity quantity :
                quantities) {
            if (quantity.getName().equals(name)) {
                return quantity;
            }
        }
        return null;
    }

    /**
     * Given an IfcElementQuantity, returns the first IfcPhysicalQuantity that is an instance of IfcQuantityArea. If
     * no such Quantity exists, throws exception.
     *
     * @param quantity An IfcElementQuantity
     * @return A single IfcQuantityArea
     */
    public static IfcQuantityArea getAreaFromElementQuantity(IfcElementQuantity quantity) {
        List<IfcPhysicalQuantity> physicalQuantities = quantity.getQuantities();
        for (IfcPhysicalQuantity physical :
                physicalQuantities) {
            if (physical instanceof IfcQuantityArea) {
                return (IfcQuantityArea) physical;
            }
        }
        throw new IllegalArgumentException("No IfcQuantityArea found in the element quantity provided");
    }

    /**
     * Given an IfcElementQuantity, returns the first IfcPhysicalQuantity that is an instance of IfcQuantityArea. If
     * no such Quantity exists, returns null.
     *
     * @param quantity An IfcElementQuantity
     * @return A single IfcQuantityArea
     */
    public static IfcQuantityArea getAreaFromElementQuantityOrNull(IfcElementQuantity quantity) {
        List<IfcPhysicalQuantity> physicalQuantities = quantity.getQuantities();
        for (IfcPhysicalQuantity physical :
                physicalQuantities) {
            if (physical instanceof IfcQuantityArea) {
                return (IfcQuantityArea) physical;
            }
        }
        return null;
    }

    /**
     * Given an IfcSpace and a name of an IfcElementQuantity on that object, returns the area found in the
     * IfcElementQuantity as a double. If no such area exists, returns 0.
     *
     * @param space An IfcSpace
     * @param name  The name of the Specific quantity, e.g. BaseQuantities
     * @return The area
     */
    public static double getAreaOfSpaceOrZero(IfcSpace space, String name) {
        List<IfcElementQuantity> quantities = ifcElementQuantitiesFromObject(space);
        IfcElementQuantity quantity = getElementQuantityFromCollectionByNameOrNull(quantities, name);
        if (quantity == null) {
            return 0;
        }
        IfcQuantityArea area = getAreaFromElementQuantityOrNull(quantity);
        if (area == null) {
            return 0;
        }
        return area.getAreaValue();

    }


    public static String getStringOfMissingProperties(IfcElement element, Set<String> correctProperties,
                                                      String name, String delimiter) {
        StringBuilder sb = new StringBuilder();
        Set<String> propertyNames = new HashSet<>();
        List<IfcPropertySet> psets = getIfcPropertySetsFromElement(element);
        IfcPropertySet pset = getPropertySetFromListByStartsWith(psets, name);
        for (IfcProperty property :
                pset.getHasProperties()) {
            propertyNames.add(property.getName());
        }
        for (String string :
                correctProperties) {
            if (!propertyNames.contains(string)) {
                sb.append(string).append(delimiter);
            }
        }
        return sb.toString();
    }

    /**
     * Given an IfcSpace and a name of an IfcElementQuantity on that object, returns the area found in the
     * IfcElementQuantity as a double. If no such area exists, indirectly throws exception.
     *
     * @param space An IfcSpace
     * @param name  The name of the Specific quantity, e.g. BaseQuantities
     * @return The area
     */
    public static double getAreaOfSpace(IfcSpace space, String name) {
        List<IfcElementQuantity> quantities = ifcElementQuantitiesFromObject(space);
        IfcElementQuantity quantity = getElementQuantityFromCollectionByName(quantities, name);
        IfcQuantityArea area = getAreaFromElementQuantity(quantity);
        return area.getAreaValue();
    }

    public static String getNameOfElement(IfcObject element) {
        if (element == null) {
            return null;
        }
        return element.getName();
    }
}

