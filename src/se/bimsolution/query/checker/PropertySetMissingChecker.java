package se.bimsolution.query.checker;

import org.bimserver.emf.IfcModelInterface;
import org.bimserver.models.ifc2x3tc1.IfcElement;
import se.bimsolution.db.object.Log;
import se.bimsolution.db.object.MissingPropertySet;
import se.bimsolution.db.object.ObjectCount;
import se.bimsolution.db.repo.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import static se.bimsolution.query.utils.QueryUtils.*;
public class PropertySetMissingChecker extends ElementChecker{
    public PropertySetMissingChecker(IfcModelInterface model, Repository repo, int revisionId, List<Class<? extends IfcElement>> classList) {
        super(model, repo, revisionId, classList);
    }

    @Override
    public void run() {
        List<IfcElement> elements = new ArrayList<>();
        for (Class clazz :
                classList) {
            elements.addAll(model.getAll(clazz));
        }
        repo.writeObjectCount(new ObjectCount(revisionId, extractNameFromClass(this.getClass()), elements.size()));

        List<MissingPropertySet> missingPropertySets = new ArrayList<>();
        for (IfcElement element :
                elements) {
            if (!getPropertySetExistsWithNameStartsWith("AH", element)) {
                missingPropertySets.add(new MissingPropertySet(
                        element.getOid(),
                        getNameOfObjectOrNull(getIfcBuildingFromElementOrNull(element)),
                        getNameOfObjectOrNull(getIfcBuildingStoreyFromElementOrNull(element)),
                        getNameOfObjectOrNull(getIfcSiteFromElementOrNull(element)),
                        extractNameFromClass(element.getClass()),
                        this.revisionId,
                        element.getName()
                ));
            }
        }
        try {
            repo.writeMissingPropertySet(missingPropertySets);
        } catch (SQLException e) {
            repo.writeLog(new Log("Something went wrong when writing missing property set: " + e.getMessage()));
        }
    }
}
