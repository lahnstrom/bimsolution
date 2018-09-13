package se.bimsolution.query.utils;

import org.bimserver.models.ifc2x3tc1.IfcElement;

public interface IElementChecker {
    boolean checkElement(IfcElement element);
}
