package se.bimsolution.query;

import org.bimserver.models.ifc2x3tc1.IfcElement;

public interface ElementChecker {
    boolean checkElement(IfcElement element);
}
