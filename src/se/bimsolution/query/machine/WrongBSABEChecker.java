package se.bimsolution.query.machine;

import org.bimserver.emf.IfcModelInterface;
import org.bimserver.models.ifc2x3tc1.IfcElement;
import org.bimserver.models.ifc2x3tc1.IfcPropertySet;
import se.bimsolution.db.Repository;

import static se.bimsolution.query.QueryUtils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class WrongBSABEChecker extends ElementChecker {
    private List<IfcElement> elements;
    List<Class> classList;
    IfcModelInterface model;
    Repository repo;
    int revisionId;

    public WrongBSABEChecker(IfcModelInterface model, Repository repo, int revisionId) {
        super(model, repo, revisionId);
    }

    public void run() {
        List<IfcElement> elements = new ArrayList<>();
        for (Class clazz :
                this.classList) {
            elements.addAll(model.getAll(clazz));
        }
    }

    public boolean hasBSABId(IfcElement element) {
        List<IfcPropertySet> propertySets = getIfcPropertySetsFromElementOrNull(element);
        if (propertySets == null) {
            return false;
        } else return true;
    }
}
