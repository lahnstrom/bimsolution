package se.bimsolution.query.machine;

import org.bimserver.emf.IfcModelInterface;
import org.bimserver.models.ifc2x3tc1.IfcSpace;
import se.bimsolution.db.Area;
import se.bimsolution.db.Log;
import se.bimsolution.db.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static se.bimsolution.query.QueryUtils.*;

public class AreaChecker extends ElementChecker{

    public AreaChecker(IfcModelInterface model, Repository repo, int revisionId, List<Class> classList) {
        super(model, repo, revisionId, classList);
    }

    @Override
    public void run() {
        List<IfcSpace> spaces = new ArrayList<>();
        List<Area> areas = new ArrayList<>();
        spaces.addAll(model.getAll(IfcSpace.class));
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
