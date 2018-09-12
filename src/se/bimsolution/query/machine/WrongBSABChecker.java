package se.bimsolution.query.machine;

import org.bimserver.emf.IfcModelInterface;
import org.bimserver.models.ifc2x3tc1.IfcElement;
import org.bimserver.models.ifc2x3tc1.IfcPropertySet;
import se.bimsolution.db.Bsab96bdMissing;
import se.bimsolution.db.Log;
import se.bimsolution.db.Repository;

import static se.bimsolution.query.QueryUtils.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class WrongBSABChecker extends ElementChecker {

    public WrongBSABChecker(IfcModelInterface model, Repository repo, int revisionId, List<Class> classList) {
        super(model, repo, revisionId, classList);
    }

    public void run() {
        List<Bsab96bdMissing> bsab96bMissings = new ArrayList<>();
        List<IfcElement> elements = new ArrayList<>();
        for (Class clazz :
                this.classList) {
            try {
                elements.addAll(model.getAll(clazz));
            } catch (Exception e) {
                e.printStackTrace();
            }
            ;
        }
        elements.forEach(element -> {
            if (!hasBsabId(element)) {


                try {
                    bsab96bMissings.add(new Bsab96bdMissing(
                            element.getOid(),
                            getIfcBuildingFromElementOrNull(element).getName(),
                            getIfcBuildingStoreyFromElementOrNull(element).getName(),
                            extractNameFromClass(element.getClass()),
                            getIfcSiteFromElement(element).getName(),
                            revisionId,
                            element.getName()));
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(element.getName());
                } ;
            }

        });
        try {
            repo.writeBsab96bdMissing(bsab96bMissings);
        } catch (SQLException e) {
            repo.writeLog(new Log(e.getMessage(), revisionId));
        }
    }

    public boolean hasBsabId(IfcElement element) {
        List<IfcPropertySet> propertySets = getIfcPropertySetsFromElementOrNull(element);
        if (propertySets == null) {
            return false;
        }
        IfcPropertySet propertySet = getPropertySetFromListByStartsWithOrNull(propertySets, "AH");
        return propertySet != null;
    }
}
