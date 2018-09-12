package se.bimsolution.query.machine;

import org.bimserver.emf.IfcModelInterface;
import org.bimserver.models.ifc2x3tc1.IfcElement;
import org.bimserver.models.ifc2x3tc1.IfcPropertySet;
import se.bimsolution.db.Bsab96bdMissing;
import se.bimsolution.db.Log;
import se.bimsolution.db.Repository;
import se.bimsolution.db.WrongStorey;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static se.bimsolution.query.QueryUtils.*;
import static se.bimsolution.query.QueryUtils.getIfcPropertySetsFromElementOrNull;
import static se.bimsolution.query.QueryUtils.getPropertySetFromListByStartsWithOrNull;
/*

public class WrongStoreyChecker {
    public WrongStoreyChecker(IfcModelInterface model, Repository repo, int revisionId, List<Class> classList) {
        super(model, repo, revisionId, classList);
    }

    public void run() {
        List<WrongStorey> wrongStoreys = new ArrayList<>();
        List<IfcElement> elements = new ArrayList<>();
        for (Class clazz :
                this.classList) {
            elements.addAll(model.getAll(clazz));
        }
        elements.forEach(element -> {
            bsab96bMissings.add(new Bsab96bdMissing(
                    element.getOid(),
                    getIfcBuildingFromElement(element).getName(),
                    getIfcBuildingStoreyFromElement(element).getName(),
                    extractNameFromClass(element.getClass()),
                    getIfcSiteFromElement(element).getName(),
                    revisionId,
                    element.getName()));
        });
        try {
            repo.writeBsab96bdMissing(bsab96bMissings);
        } catch (SQLException e) {
            repo.writeLog(new Log(e.getMessage(), revisionId));
        }
    }

    public boolean hasBsabId(IfcElement element) {
        List<IfcPropertySet> propertySets = getIfcPropertySetsFromElementOrNull(element);
        IfcPropertySet propertySet = getPropertySetFromListByStartsWithOrNull(propertySets, "AH");
        return propertySet != null;
    }


}
*/