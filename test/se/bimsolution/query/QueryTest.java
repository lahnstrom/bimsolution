package se.bimsolution.query;

import org.bimserver.client.BimServerClient;
import org.bimserver.emf.IfcModelInterface;
import org.bimserver.models.ifc2x3tc1.IfcDoor;
import org.bimserver.models.ifc2x3tc1.IfcPropertySet;
import org.bimserver.models.ifc2x3tc1.IfcPropertySingleValue;
import org.bimserver.shared.UsernamePasswordAuthenticationInfo;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.*;

public class QueryTest {

    private IfcModelInterface model;
    private IfcDoor door;

    @Before
    public void setUp() throws Exception {
        List<String> creds = new ArrayList<>();
        Scanner sc = new Scanner(new File("cred.txt"));
        while (sc.hasNext()) {
            creds.add(sc.next());
        }

        BimServerClient bsc = new ClientBuilder(
                new UsernamePasswordAuthenticationInfo(creds.get(1), creds.get(2)), creds.get(0)).build();
        this.model = new ModelBuilder(bsc, creds.get(3)).build();
        this.door = model.getAll(IfcDoor.class).get(0);

    }

    @Test
    public void ifcBuildingStoreyFromElement() {
        assertNotNull(QueryUtils.ifcBuildingStoreyFromElement(door).getName());
    }

    @Test
    public void ifcBuildingFromElement() {
        assertNotNull(QueryUtils.ifcBuildingFromElement(door).getName());
    }

    @Test
    public void ifcSiteFromElement() {
        assertNotNull(QueryUtils.ifcSiteFromElement(door).getName());
    }

    @Test
    public void ifcPropertySetsFromElement() {
        assertNotNull(QueryUtils.ifcPropertySetsFromElement(door).get(0).getName());
    }

    @Test
    public void getPropertySetByName() {
        List<IfcPropertySet> psets = QueryUtils.ifcPropertySetsFromElement(door);
        assertNotNull(QueryUtils.getPropertySetByName(psets, "AH-Bygg").getName());
    }

    @Test
    public void getPropertySetByStartsWith() {
        List<IfcPropertySet> psets = QueryUtils.ifcPropertySetsFromElement(door);
        assertNotNull(QueryUtils.getPropertySetByStartsWith(psets, "AH").getName());
    }

    @Test
    public void getSingleValueByName() {
        List<IfcPropertySet> psets = QueryUtils.ifcPropertySetsFromElement(door);
        IfcPropertySet pset = QueryUtils.getPropertySetByStartsWith(psets, "AH");
        assertNotNull(QueryUtils.getSingleValueByName(pset, "BSAB96BD"));
    }

    @Test
    public void getNominalTextValueFromSingleValue() {
        List<IfcPropertySet> psets = QueryUtils.ifcPropertySetsFromElement(door);
        IfcPropertySet pset = QueryUtils.getPropertySetByStartsWith(psets, "AH");
        IfcPropertySingleValue psv = QueryUtils.getSingleValueByName(pset, "BSAB96BD");
        assertNotNull(QueryUtils.getNominalTextValueFromSingleValue(psv));
    }

    @Test
    public void propertyExistsInPropertySet() {
        List<IfcPropertySet> psets = QueryUtils.ifcPropertySetsFromElement(door);
        IfcPropertySet pset = QueryUtils.getPropertySetByStartsWith(psets, "AH");
        assertTrue(QueryUtils.propertyExistsInPropertySet(pset, "BSAB96BD"));

    }
}