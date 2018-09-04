package se.bimsolution.query.machine;

import org.bimserver.emf.IdEObject;
import org.bimserver.emf.IfcModelInterface;
import org.bimserver.models.ifc2x3tc1.*;
import org.eclipse.emf.common.util.EList;
import se.bimsolution.db.Fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class IdValidationMachine implements QueryMachine {

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

    private final String PROPERTY_SET_NAME = "AH-Bygg";
    private final String PROPERTY_NAME = "BSAB96BD";

    public IdValidationMachine(IfcModelInterface model, int queryID, int runID) {
        this.model = model;
        this.queryID = queryID;
        this.runID = runID;
    }

    @Override
    public List<Fail> getFails() {
        if (!hasRun) {
            throw new IllegalStateException("The machine must run before calling the getFails method.");
        } else if (error != null) {
            throw new IllegalStateException("The machine's run method resulted in an error being thrown. " +
                    "Therefore this method can't be called");
        }
        return null;
    }

    @Override
    public int getCount() {
        if (!hasRun) {
            throw new IllegalStateException("The machine must run before calling the getCount method.");
        } else if (error != null) {
            throw new IllegalStateException("The machine's run method resulted in an error being thrown. " +
                    "Therefore this method can't be called");
        }
        return 0;
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

    private void populateClassList() {
        //TODO This method should populate the list of classes
        classList.add(IfcDoor.class);
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
        List<IdEObject> objList = model.getAllWithSubTypes(clazz);
        for (IdEObject obj :
                objList) {
            checkIdEObject(clazz, localFails, obj);
        }
        return localFails;
    }

    /**
     * This method serves as an intermediate step in the process of checking a BIM Object
     * It checks that the object is actually an IfcObject, otherwise it can't relate to a IfcRelDefinesBy object.
     * It then loops through  all the IfcRelDefines in the object and examines them further.
     * @param clazz The class representing an ifc 2x3 object.
     * @param localFails the list of fails for this specific object class
     * @param obj the BIM Object that is being checked.
     */
    private void checkIdEObject(Class<IdEObject> clazz, List<Fail> localFails, IdEObject obj) {
        if (!(obj instanceof IfcObject)) { throw new IllegalStateException(clazz.getName() + " is not an instance of IfcObject.");}
        EList<IfcRelDefines> ifcRelDefinesEList = ((IfcObject) obj).getIsDefinedBy();
        //Gå igenom alla defines by relationer
        for (IfcRelDefines ird : ifcRelDefinesEList) {
            boolean hasCorrectPropertySet = checkIfcRelDefines(clazz, localFails, obj, ird);
            if (!hasCorrectPropertySet) {
                localFails.add(new Fail(obj.getOid(), this.queryID, this.runID));
            }
        }
    }

    /**
     * Checks all the IfcRelDefines relations of an IfcObject.
     * Returns false if an object does not contain a property.
     * @param clazz The class representing an ifc 2x3 object.
     * @param localFails the list of fails for this specific object class
     * @param obj the BIM Object that is being checked.
     * @param ird an IfcRelDefinesBy object
     * @return Is there a propertySet with the asked for name? && Is it correctly filled in?
     */
    private boolean checkIfcRelDefines(Class<IdEObject> clazz, List<Fail> localFails, IdEObject obj, IfcRelDefines ird) {
        IfcPropertySet ps;
        boolean psetCorrect = false;
        //Kolla om det är en definesbypropertyrelation
        if (ird instanceof IfcRelDefinesByProperties) {
            //Går den att casta till ett propertyset?
            if (((IfcRelDefinesByProperties) ird).getRelatingPropertyDefinition() instanceof IfcPropertySet)  {
                ps = (IfcPropertySet) ((IfcRelDefinesByProperties) ird).getRelatingPropertyDefinition();
                //Är det rätt propertyset?
                if (ps.getName().equals(PROPERTY_SET_NAME)) {
                    //Gå igenom alla properties i setet, om någon är fel
                    psetCorrect = loopThroughProperties(clazz, localFails, obj, ps);
                }
            }
        }
        return psetCorrect;
    }

    /**This method loops through a propertySet and checks every value.
     * If the propertySet is not correct, return false;
     *
     * @param clazz The class representing an ifc 2x3 object.
     * @param localFails the list of fails for this specific object class
     * @param obj the BIM Object that is being checked.
     * @param ps The propertySet to loop through
     * @return Does the property we're looking for exist?
     */
    private boolean loopThroughProperties(Class<IdEObject> clazz, List<Fail> localFails, IdEObject obj, IfcPropertySet ps) {
        boolean existsProperty = false;
        for (IfcProperty ip : ps.getHasProperties()) {
            if (checkPropertyAndAddToFailList(clazz, localFails, obj, ip)) {
                existsProperty = true;
            }
        }
        return existsProperty;
    }

    /**
     *This method executes the final check of an IfcObject:
     * Is there a property? Does it have the type single value?
     * If so, is the name of the property what we're looking for?
     * If so, is it correctly filled in?
     * If not. Add it to our list of fails.
     * @param clazz The class representing an ifc 2x3 object.
     * @param localFails the list of fails for this specific object class
     * @param obj the BIM Object that is being checked.
     * @param ip an IfcProperty to be checked.
     * @return Does the correct PSet have a field for ID?
     */
    private boolean checkPropertyAndAddToFailList(Class<IdEObject> clazz, List<Fail> localFails, IdEObject obj, IfcProperty ip) {
        boolean hasProp = false;
        if (ip instanceof IfcPropertySingleValue) {
            if (ip.getName().equals(PROPERTY_NAME)) {
                hasProp = true;
                IfcValue value = ((IfcPropertySingleValue) ip).getNominalValue();
                String id = "";
                //Ta ut textvärdet om det finns
                if (value instanceof IfcText) {
                    id = ((IfcText)value).getWrappedValue();
                }
                checkIfValuePasses(clazz, localFails, obj, id);
            }
        }
        return hasProp;
    }

    /**
     *Check the value that we've extracted against the hashMap.
     * If it fails: add it to the list of fails.
     * @param clazz The class representing an ifc 2x3 object.
     * @param localFails the list of fails for this specific object class
     * @param obj the BIM Object that is being checked.
     * @param id the value that we extracted, and that proved to be wrong.
     */
    private void checkIfValuePasses(Class<IdEObject> clazz, List<Fail> localFails, IdEObject obj, String id) {
        String name = extractNameFromClass(clazz);
        if (!correctIDs.get(name).contains(id)) {
            localFails.add(new Fail(obj.getOid(), this.queryID, this.runID));
            this.failCount++;
        }
    }


    /**
     * Takes a name of an IfcClass and extracts the name e.g.
     * class org.bimserver.models.ifc2x3tc1.IfcDoor turns into string
     * IfcDoor
     * This is then used to look up the correct ID in a HashMap.
     * @param clazz a java class
     * @return a string, the name of the Ifc class.
     */
    private String extractNameFromClass(Class<IdEObject> clazz) {
        String[] parts = clazz.toString().split("\\.");
        return parts[parts.length-1];
    }

    @Override
    public void run() {
        hasRun = true;
        for (Class clazz :
                classList) {
            try {
                fails.addAll(runCheckForOneClass(clazz));
            } catch (IllegalStateException e) {
                this.error = e.getMessage();
            }
        }
    }
}
