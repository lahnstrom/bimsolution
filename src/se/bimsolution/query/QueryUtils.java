package se.bimsolution.query;

import org.bimserver.models.ifc2x3tc1.*;

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
//        classList.add(IfcSlab.class);
        classList.add(IfcDamperType.class);
        classList.add(IfcRoof.class);
        classList.add(IfcSite.class);
        classList.add(IfcJunctionBoxType.class);
//        classList.add(IfcSpace.class);
        classList.add(IfcOutletType.class);
        classList.add(IfcSystem.class);
        classList.add(IfcValveType.class);
        classList.add(IfcWall.class);
        classList.add(IfcFlowController.class);
        classList.add(IfcLightFixtureType.class);
//        classList.add(IfcWindow.class);
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
}
