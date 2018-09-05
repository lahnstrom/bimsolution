package se.bimsolution.query.machine;

import org.bimserver.emf.IdEObject;
import org.bimserver.emf.IfcModelInterface;
import org.bimserver.models.ifc2x3tc1.*;
import org.eclipse.emf.common.util.EList;
import se.bimsolution.db.Fail;
import se.bimsolution.query.QueryUtils;

import java.io.IOException;
import java.util.*;

public class IdValidationMachine implements QueryMachine {

    private final int QUERY_ID = 1;
    private int runID;
    private int queryID;
    private IfcModelInterface model;
    private List<Fail> fails;
    private int count;
    private int failCount;
    private String error;
    private boolean hasRun;
    private List<Class> classList;
    private HashMap<String, HashSet<String>> correctIDs;

    private final String VARIES_STRING = "Varierar";
    private final String PROPERTY_SET_NAME = "AH";
    private final String PROPERTY_NAME = "BSAB96BD";


    private int debugCount;
    private int debugCount2;
    private int debugCount3;

    public IdValidationMachine(IfcModelInterface model, int queryID, int runID) {
        this.model = model;
        this.queryID = queryID;
        this.runID = runID;
        this.fails = new ArrayList<>();
        try {
            this.correctIDs = QueryUtils.idToIfcCSVParser("resources\\spec.csv", ",");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getID() {
        return QUERY_ID;
    }

    @Override
    public List<Fail> getFails() {
        if (!hasRun) {
            throw new IllegalStateException("The machine must run before calling the getFails method.");
        } else if (error != null) {
            throw new IllegalStateException("The machine's run method resulted in an error being thrown. " +
                    "Therefore this method can't be called");
        }
        return this.fails;
    }

    @Override
    public int getCount() {
        if (!hasRun) {
            throw new IllegalStateException("The machine must run before calling the getCount method.");
        } else if (error != null) {
            throw new IllegalStateException("The machine's run method resulted in an error being thrown. " +
                    "Therefore this method can't be called");
        }
        return this.count;
    }

    @Override
    public int getFailCount() {
        if (!hasRun) {
            throw new IllegalStateException("The machine must run before calling the getFailCount method.");
        } else if (error != null) {
            throw new IllegalStateException("The machine's run method resulted in an error being thrown. " +
                    "Therefore this method can't be called");
        }
        return this.failCount;
    }

    @Override
    public String getError() {
        if (!hasRun) {
            throw new IllegalStateException("The machine must run before calling the getError method.");
        }
        return this.error;
    }

    //TODO Refactor the machine to take a classlist.
    private void populateClassList() {
        classList = new ArrayList<>();
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
    }


    /**
     * This method checks all objects of a certain Ifc 2x3 class
     * It returns all the fails in the object group, ready to be written to DB.
     *
     * @param clazz the class to check, i.e. IfcDoor
     * @return The fails for the objects that do not pass the check.
     */
    private List<Fail> runCheckForOneClass(Class<org.bimserver.emf.IdEObject> clazz) {
        List<Fail> localFails = new ArrayList<>();
        List<IdEObject> objList = model.getAll(clazz);
        for (IdEObject obj :
                objList) {
            checkIdEObject(clazz, localFails, obj);
            this.count++;
        }
        return localFails;
    }

    /**
     * This method serves as an intermediate step in the process of checking a BIM Object
     * It checks that the object is actually an IfcObject, otherwise it can't relate to a IfcRelDefinesBy object.
     * It then loops through  all the IfcRelDefines in the object and examines them further.
     *
     * @param clazz      The class representing an ifc 2x3 object.
     * @param localFails the list of fails for this specific object class
     * @param obj        the BIM Object that is being checked.
     */
    private void checkIdEObject(Class<IdEObject> clazz, List<Fail> localFails, IdEObject obj) {
        if (!(obj instanceof IfcObject)) {
            throw new IllegalStateException(clazz.getName() + " is not an instance of IfcObject.");
        }
        EList<IfcRelDefines> ifcRelDefinesEList = ((IfcObject) obj).getIsDefinedBy();
        boolean hasPset = false;
        for (IfcRelDefines ird : ifcRelDefinesEList) {
            if (checkIfcRelDefines(clazz, localFails, obj, ird)) {
                hasPset = true;
                break;
            }
        }
        if (!hasPset) {
            addNewFail(localFails, obj);
            debugCount2++;
        }
    }

    /**
     * Checks all the IfcRelDefines relations of an IfcObject.
     * Returns false if an object does not contain a property.
     *
     * @param clazz      The class representing an ifc 2x3 object.
     * @param localFails the list of fails for this specific object class
     * @param obj        the BIM Object that is being checked.
     * @param ird        an IfcRelDefinesBy object
     * @return Is there a propertySet with the asked for name? && Is it correctly filled in?
     */
    private boolean checkIfcRelDefines(Class<IdEObject> clazz, List<Fail> localFails, IdEObject obj, IfcRelDefines ird) {
        IfcPropertySet ps;
        boolean hasPset = false;
        if (ird instanceof IfcRelDefinesByProperties) {
            if (((IfcRelDefinesByProperties) ird).getRelatingPropertyDefinition() instanceof IfcPropertySet) {
                ps = (IfcPropertySet) ((IfcRelDefinesByProperties) ird).getRelatingPropertyDefinition();
                if (ps.getName().startsWith(PROPERTY_SET_NAME)) {
                    hasPset = true;
                    loopThroughProperties(clazz, localFails, obj, ps);
                }
            }
        }
        return hasPset;
    }

    /**
     * This method loops through a propertySet and checks every value.
     * If the propertySet is not correct, return false;
     *
     * @param clazz      The class representing an ifc 2x3 object.
     * @param localFails the list of fails for this specific object class
     * @param obj        the BIM Object that is being checked.
     * @param ps         The propertySet to loop through
     */
    private void loopThroughProperties(Class<IdEObject> clazz, List<Fail> localFails, IdEObject obj, IfcPropertySet ps) {
        boolean rightPSetExistsOnObject = false;
        for (IfcProperty ip : ps.getHasProperties()) {
            if (checkPropertyAndAddToFailList(clazz, localFails, obj, ip)) {
                rightPSetExistsOnObject = true;
                break;
            }
        }
        if (!rightPSetExistsOnObject) {
            addNewFail(localFails, obj);
            debugCount3++;
        }
    }

    /**
     * This method executes the final check of an IfcObject:
     * Is there a property? Does it have the type single value?
     * If so, is the name of the property what we're looking for?
     * If so, is it correctly filled in?
     * If not. Add it to our list of fails.
     *
     * @param clazz      The class representing an ifc 2x3 object.
     * @param localFails the list of fails for this specific object class
     * @param obj        the BIM Object that is being checked.
     * @param ip         an IfcProperty to be checked.
     * @return Is this the pset we're looking for
     */
    private boolean checkPropertyAndAddToFailList(Class<IdEObject> clazz, List<Fail> localFails, IdEObject obj, IfcProperty ip) {
        boolean isRightPSet = false;
        if (ip instanceof IfcPropertySingleValue) {
            if (ip.getName().equals(PROPERTY_NAME)) {
                isRightPSet = true;
                IfcValue value = ((IfcPropertySingleValue) ip).getNominalValue();
                String textValue;
                //Ta ut textvärdet om det finns
                if (value instanceof IfcText) {
                    textValue = ((IfcText) value).getWrappedValue();
                    checkIfValuePasses(clazz, localFails, obj, textValue);
                }
            }
        }
        return isRightPSet;
    }

    /**
     * Check the value that we've extracted against the hashMap.
     * If it fails: add it to the list of fails.
     *
     * @param clazz      The class representing an ifc 2x3 object.
     * @param localFails the list of fails for this specific object class
     * @param obj        the BIM Object that is being checked.
     * @param textValue  the value that we extracted, and that proved to be wrong.
     */
    private void checkIfValuePasses(Class<IdEObject> clazz, List<Fail> localFails, IdEObject obj, String textValue) {
        String name = extractNameFromClass(clazz);
        this.debugCount++;

        if (!correctIDs.get(name).contains(textValue)) {
            if (!correctIDs.get(VARIES_STRING).contains(textValue)) {
                addNewFail(localFails, obj);
//                System.out.println("I failed: " + textValue + " My object id is: " + obj.getOid() + "  I am an " + extractNameFromClass(clazz));
//                System.out.print("The correct IDs are: ");
//                correctIDs.get(name).forEach(x-> System.out.print(x + " |  "));
//                System.out.println();
            }
        }

    }

    private void addNewFail(List<Fail> localFails, IdEObject obj) {
        localFails.add(new Fail(obj.getOid(), this.queryID, this.runID));
        this.failCount++;
    }


    /**
     * Takes a name of an IfcClass and extracts the name e.g.
     * class org.bimserver.models.ifc2x3tc1.IfcDoor turns into string
     * IfcDoor
     * This is then used to look up the correct ID in a HashMap.
     *
     * @param clazz a java class
     * @return a string, the name of the Ifc class.
     */
    private String extractNameFromClass(Class<IdEObject> clazz) {
        String[] parts = clazz.toString().split("\\.");
        String ret = parts[parts.length - 1];
        if (ret.endsWith("Type")) {
            ret = ret.substring(0, ret.length() - 4);
        }
        if (ret.endsWith("Element")) {
            ret = ret.substring(0, ret.length() - 7);
        }
        return ret;
    }

    @Override
    public void run() {
        populateClassList();
        hasRun = true;
        for (Class clazz :
                classList) {
            try {
                fails.addAll(runCheckForOneClass(clazz));
            } catch (IllegalStateException e) {
                this.error = e.getMessage();
            }
        }
        debugPrint();
    }



    private void debugPrint() {
        System.out.println("I checked the following IfcObjects in the model");
        for (Class clazz:
             classList) {
            System.out.println(extractNameFromClass(clazz));
        }
        System.out.println("I handled this many object: " + count);
        System.out.println("I got this many  fails: " + failCount);
        System.out.println("This many objects made it to the last method: " + debugCount);
        System.out.println("This many objects are missing AH psets: " + debugCount2);
        System.out.println("This many objects have AH psets but are missing BSAB96BD values: " + debugCount3);
    }
}
