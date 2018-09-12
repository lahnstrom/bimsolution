package se.bimsolution.query.machine;


import org.bimserver.emf.IfcModelInterface;
import org.bimserver.models.ifc2x3tc1.IfcBuildingStorey;
import org.bimserver.models.ifc2x3tc1.IfcElement;
import se.bimsolution.db.Log;
import se.bimsolution.db.Repository;
import se.bimsolution.db.WrongStorey;
import se.bimsolution.query.QueryUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static se.bimsolution.query.QueryUtils.*;
public class WrongStoreyChecker extends ElementChecker{


    public static final double THRESHOLD = 2000;

    public WrongStoreyChecker(IfcModelInterface model, Repository repo, int revisionId, List<Class> classList) {
        super(model, repo, revisionId, classList);
    }



    @Override
    public void run() {
        List<IfcElement> elements = new ArrayList<>();
        for (Class clazz:
             classList) {
            elements.addAll(model.getAll(clazz));
        }
        List<IfcBuildingStorey> storeys = model.getAll(IfcBuildingStorey.class);
        List<WrongStorey> wrongs = new ArrayList<>();
        for (IfcElement element:
             elements) {
            if (getElementIsOnWrongStorey(element, storeys, THRESHOLD)) {
                wrongs.add(new WrongStorey(
                        element.getOid(),
                        getNameOfObjectOrNull(getIfcBuildingStoreyFromElement(element)),
                        getNameOfObjectOrNull(getIfcBuildingFromElement(element)),
                        getNameOfObjectOrNull(getIfcSiteFromElement(element)),
                        extractNameFromClass(element.getClass()),
                        revisionId,
                        element.getName()
                ));
            }
        }
        try {
            repo.writeWrongStorey(wrongs);
        } catch (SQLException e) {
            repo.writeLog(new Log("Something went wrong when writing WrongStoreys: " + e.getMessage()));
        }

    }
}
