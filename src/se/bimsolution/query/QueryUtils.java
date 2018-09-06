package se.bimsolution.query;

import org.bimserver.emf.IdEObject;
import org.bimserver.models.ifc2x3tc1.*;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EContentsEList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Utility class for QueryMachines
 */
public final class QueryUtils {


    private QueryUtils() {}

    /**
     * The classes we're checking in the Query Machines
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
    public static HashMap<String, HashSet<String>> idToIfcCSVParser (String CSVfilepath, String delimiter) throws IOException {
        String line;
        HashMap<String, HashSet<String>> parsedData = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(CSVfilepath));
        while((line=br.readLine())!=null){
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



    //TODO IfcBuilding from IfcElement
    public IfcBuildingStorey ifcBuildingStoreyFromElement(IfcElement element) {
        EList<IfcRelContainedInSpatialStructure> relList = element.getContainedInStructure();
        for (IfcRelContainedInSpatialStructure rel:
                relList) {
            if (rel.getRelatingStructure() instanceof IfcBuildingStorey) {
                return (IfcBuildingStorey) rel.getRelatingStructure();
            }
        }
        throw new IllegalArgumentException("The element has no IfcBuildingStorey associated with it");
    }

    //TODO IfcStorey from IfcOBject
    public IfcBuilding ifcBuildingFromElement(IfcElement element) {
        IfcBuildingStorey storey = ifcBuildingStoreyFromElement(element);
        EList<IfcRelDecomposes> decomposes = storey.getDecomposes();
        for (IfcRelDecomposes de : decomposes) {
            if (de.getRelatingObject() instanceof IfcBuilding) {
                return (IfcBuilding) de.getRelatingObject();
            }
        }
        throw new IllegalArgumentException("The element has no IfcBuilding associated with it");

    }

    //TODO IfcPsets from IfcObject
    public EList<IfcPropertySet> ifcPropertySetFromElement(IfcObject element) {
        EList<IfcRelDefines> definesList = element.getIsDefinedBy();
        EList<IfcPropertySet> psets = new BasicEList<>();
        for (IfcRelDefines rel:
             definesList) {
            if (rel instanceof IfcRelDefinesByProperties) {
                IfcPropertySetDefinition pset = ((IfcRelDefinesByProperties) rel).getRelatingPropertyDefinition();
                if (pset instanceof IfcPropertySet) {
                    psets.add((IfcPropertySet) pset);
                }
            }
        }
        if (psets.size()==0) {
            throw new IllegalArgumentException("The element has no relating IfcPropertySet");
        }
        return psets;
    }

    public IfcPropertySet getPropertySetByName(List<IfcPropertySet> psets, String name) {
        for (IfcPropertySet pset:
             psets) {
            if (pset.getName().equals(name)) {
                return pset;
            }
        }
        throw new IllegalArgumentException("The property set with name " + name + " does not exist");
    }

    public IfcPropertySet getPropertySetByStartsWith(List<IfcPropertySet> propertySets ,String startsWith) {
        for (IfcPropertySet pset:
                propertySets) {
            if (pset.getName().startsWith(startsWith)) {
                return pset;
            }
        }
        throw new IllegalArgumentException("No property starting with " + startsWith + " exist");
    }


    public IfcPropertySingleValue getSingleValueByName(IfcPropertySet ifcPropertySet, String name) {
        EList<IfcProperty> properties = ifcPropertySet.getHasProperties();
        for (IfcProperty prop:
             properties) {
            if (prop instanceof IfcPropertySingleValue && prop.getName().equals(name)) {
                return (IfcPropertySingleValue) prop;
            }
        }
        throw new IllegalArgumentException("No IfcProperty found in the IfcPropertySet with the name: " + name);
    }
    //TODO Property with name NAME from pset:

    public String getNominalTextValueFromSingleValue(IfcPropertySingleValue singleValue) {
        IfcValue value = singleValue.getNominalValue();
        //Ta ut textvärdet om det finns
        if (value instanceof IfcText) {
            return ((IfcText) value).getWrappedValue();
        }
        throw new IllegalArgumentException("The IfcPropertySingleValue does not have a nominal value with type text");
    }

    //TODO BSAB96BD from pset

    //TODO TYPID from pset

    //TODO Benämning from pset


}
