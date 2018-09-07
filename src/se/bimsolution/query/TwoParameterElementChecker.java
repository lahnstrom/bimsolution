package se.bimsolution.query;

import org.bimserver.models.ifc2x3tc1.IfcElement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public interface TwoParameterElementChecker {
    boolean checkElement(Map<String, HashSet<String>> map, IfcElement element);
}