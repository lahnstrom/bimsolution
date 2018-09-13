package se.bimsolution.query.checker;

import org.bimserver.emf.IfcModelInterface;
import org.bimserver.models.ifc2x3tc1.IfcElement;
import org.bimserver.models.ifc2x3tc1.IfcSpace;
import se.bimsolution.db.object.Area;
import se.bimsolution.db.object.Log;
import se.bimsolution.db.object.ObjectCount;
import se.bimsolution.db.repo.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static se.bimsolution.query.utils.QueryUtils.*;

public class AreaChecker extends ElementChecker{

    public AreaChecker(IfcModelInterface model, Repository repo, int revisionId, List<Class<? extends IfcElement>> classList) {
        super(model, repo, revisionId, classList);
    }

    @Override
    public void run() {
        List<IfcSpace> spaces = new ArrayList<>();
        List<Area> areas = new ArrayList<>();
        spaces.addAll(model.getAll(IfcSpace.class));
        repo.writeObjectCount(new ObjectCount(revisionId, extractNameFromClass(this.getClass()), spaces.size()));

        for (IfcSpace space:
             spaces) {
            areas.add(new Area(space.getOid(),
                    getNameOfObjectOrNull(getIfcBuildingFromIfcSpaceOrNull(space)),
                    getNameOfObjectOrNull(getIfcStoreyFromIfcSpaceOrNull(space)),
                    getNameOfObjectOrNull(getIfcSiteFromIfcSpaceOrNull(space)),
                    getAreaOfSpace(space, "BaseQuantities"),
                    this.revisionId,
                    space.getName()
            ));
        }
        try {
            repo.writeArea(areas);
        } catch (SQLException e) {
            repo.writeLog(new Log("Failed in writing areas: " + e.getMessage()));
        }
    }
}
