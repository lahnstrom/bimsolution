package se.bimsolution.query;
import org.bimserver.models.ifc2x3tc1.IfcDoor;
import org.bimserver.models.ifc2x3tc1.IfcPropertySet;
import org.bimserver.models.ifc2x3tc1.IfcPropertySingleValue;
import org.bimserver.models.ifc2x3tc1.IfcValue;

import java.util.List;

import static se.bimsolution.query.QueryUtils.*;


public class ElementCheckerUtils {



    /**
     * Given an ifc element, return true if it has a property set, otherwise return false.
     *
     */

    static ElementChecker hasPropertySetExist = element -> {
        try{
            ifcPropertySetsFromElement(element);
            return true;
        }
        catch(Exception e){
            return false;
        }
    };


    /**
     * Given an ifc element with a property set. Does its property set contain a BSBAP ID?
     * If yes, return true, else return false.
     *
     */

    static ElementChecker hasBsabId = element -> {
           List<IfcPropertySet> propertySets = ifcPropertySetsFromElement(element);
           try{
               getPropertySetByStartsWith(propertySets,"AH-bygg");
               return true;
           }
           catch(Exception e){
               return false;
           }
    };

    /**
     * Given an ifc element with a property set that contains a BSBAP ID. Is the BSBAP ID correct?
     * If yes, return true, else return false.
     *
     */

    static TwoParameterElementChecker isBasabIdCorrect = (map, element) -> {

        List<IfcPropertySet> propertySets = ifcPropertySetsFromElement(element);
        IfcPropertySet propertySet = getPropertySetByStartsWith(propertySets,"AH-Bygg");
        //IfcValue bsabId = getSingleValueByName(propertySet,"BSAB96BD");




        return true;
    }

    /**
     * Given an ifc element, return true if it's on the associated storey, otherwise return false.
     *
     */


    static ElementChecker isObjectOnCorrectFloor = element -> {
        if (elementIsBelowFloorLevel(element)){
            return false;
        }
        if(getHeightDifferenceBetweenStoreyAndElement(element) -
                getHeightOfStorey(ifcBuildingStoreyFromElement(element)) > 0.1){
            return false;
        }
        return true;
    };

}
