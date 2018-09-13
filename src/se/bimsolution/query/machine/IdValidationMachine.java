/*
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

    private int runID;
    private IfcModelInterface model;
    private List<Fail> fails;
    private int count;
    private int failCount;
    private String error;
    private boolean hasRun;
    private List<Class> classList;
    private HashMap<String, HashSet<String>> correctIDs;

    private final int QUERY_ID = 1;
    private final String VARIES_STRING = "Varierar";
    private final String PROPERTY_SET_NAME = "AH";
    private final String PROPERTY_NAME = "BSAB96BD";


    private int debugCount;
    private int debugCount2;
    private int debugCount3;

    public IdValidationMachine(IfcModelInterface model, int runID) {
        this.model = model;
        this.runID = runID;
        this.fails = new ArrayList<>();
        this.classList = QueryUtils.getElementClassList();
        try {
            this.correctIDs = QueryUtils.getHashMapByIdToIfcCSVParsing("resources\\spec.csv", ",");
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


    */
/**
     * This method checks all objects of a certain Ifc 2x3 class
     * It returns all the fails in the object group, ready to be written to DB.
     *
     * @param clazz the class to check, i.e. IfcDoor
     * @return The fails for the objects that do not pass the check.
     *//*

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

    */
/**
     * This method serves as an intermediate step in the process of checking a BIM Object
     * It checks that the object is actually an IfcObject, otherwise it can't relate to a IfcRelDefinesBy object.
     * It then loops through  all the IfcRelDefines in the object and examines them further.
     *
     * @param clazz      The class representing an ifc 2x3 object.
     * @param localFails the list of fails for this specific object class
     * @param obj        the BIM Object that is being checked.
     *//*

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
            addNewFail(localFails, obj, clazz);
            System.out.println("I don't have pset: " + extractNameFromClass(clazz));
            debugCount2++;
        }
    }

    */
/**
     * Checks all the IfcRelDefines relations of an IfcObject.
     * Returns false if an object does not contain a property.
     *
     * @param clazz      The class representing an ifc 2x3 object.
     * @param localFails the list of fails for this specific object class
     * @param obj        the BIM Object that is being checked.
     * @param ird        an IfcRelDefinesBy object
     * @return Is there a propertySet with the asked for name? && Is it correctly filled in?
     *//*

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

    */
/**
     * This method loops through a propertySet and checks every value.
     * If the propertySet is not correct, return false;
     *
     * @param clazz      The class representing an ifc 2x3 object.
     * @param localFails the list of fails for this specific object class
     * @param obj        the BIM Object that is being checked.
     * @param ps         The propertySet to loop through
     *//*

    private void loopThroughProperties(Class<IdEObject> clazz, List<Fail> localFails, IdEObject obj, IfcPropertySet ps) {
        boolean rightPSetExistsOnObject = false;
        for (IfcProperty ip : ps.getHasProperties()) {
            if (checkPropertyAndAddToFailList(clazz, localFails, obj, ip)) {
                rightPSetExistsOnObject = true;
                break;
            }
        }
        if (!rightPSetExistsOnObject) {
            addNewFail(localFails, obj, clazz);
            System.out.println("I don't have a BSAB96BD value: " + extractNameFromClass(clazz) + " OID: " + obj.getOid());

            debugCount3++;
        }
    }

    */
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
     *//*

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

    */
/**
     * Check the value that we've extracted against the hashMap.
     * If it fails: add it to the list of fails.
     *
     * @param clazz      The class representing an ifc 2x3 object.
     * @param localFails the list of fails for this specific object class
     * @param obj        the BIM Object that is being checked.
     * @param textValue  the value that we extracted, and that proved to be wrong.
     *//*

    private void checkIfValuePasses(Class<IdEObject> clazz, List<Fail> localFails, IdEObject obj, String textValue) {
        String name = extractNameFromClass(clazz);
        this.debugCount++;

        if (!correctIDs.get(name).contains(textValue)) {
            if (!correctIDs.get(VARIES_STRING).contains(textValue)) {
                addNewFail(localFails, obj, clazz);
                failDebugPrint(clazz, obj, textValue, name);
            }
        }

    }

    private void failDebugPrint(Class<IdEObject> clazz, IdEObject obj, String textValue, String name) {
        System.out.println("Failure for oid: " + obj.getOid() + ", an " + extractNameFromClass(clazz)
                + ", The object has BSAB96BD id " + textValue );
        System.out.print("The correct IDs for "+ extractNameFromClass(clazz) + " are: ");
        correctIDs.get(name).forEach(x-> System.out.print(x + " |  "));
        System.out.println();
    }



    private void addNewFail(List<Fail> localFails, IdEObject obj, Class clazz) {
        localFails.add(new Fail(obj.getOid(), QUERY_ID, this.runID, extractNameFromClass(clazz)));
        this.failCount++;
    }


    */
/*

    @Override
    public void run() {
        for (Class clazz :
                classList) {
            try {
                fails.addAll(runCheckForOneClass(clazz));
            } catch (IllegalStateException e) {
                this.error = e.getMessage();
            }
        }
        hasRun = true;
        debugPrint();
    }

    //TODO
    @Override
    public int getErrorId() {
        return 0;
    }


    */
/**
     * Writes some debug information to console
     *//*

    private void debugPrint() {
        System.out.println("I checked the following IfcObjects in the model");
        for (Class clazz:
             classList) {
            System.out.println(extractNameFromClass(clazz));
        }
        System.out.println("I handled this many object: " + count);
        System.out.println("This many objects are missing AH psets: " + debugCount2);
        System.out.println("This many objects have AH psets but are missing BSAB96BD values: " + debugCount3);
        System.out.println("This many objects have psets that have BSAB96BD values: " + debugCount);
        System.out.println("I got this many incorrect IDs: " + failCount);
    }
}
*/
