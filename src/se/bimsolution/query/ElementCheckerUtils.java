package se.bimsolution.query;
import static se.bimsolution.query.QueryUtils.*;

public class ElementCheckerUtils {

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
