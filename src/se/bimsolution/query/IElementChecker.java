package se.bimsolution.query;

import org.bimserver.models.ifc2x3tc1.IfcElement;

public interface IElementChecker {
    boolean checkElement(IfcElement element);
}
