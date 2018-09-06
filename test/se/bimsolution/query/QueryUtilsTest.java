package se.bimsolution.query;

import org.bimserver.client.BimServerClient;
import org.bimserver.emf.IfcModelInterface;
import org.bimserver.shared.UsernamePasswordAuthenticationInfo;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class QueryUtilsTest {
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(QueryUtils.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    private IfcModelInterface model;

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
    }

    @Test
    public void ifcBuildingStoreyFromElement() {
        QueryUtils.
    }

    @Test
    public void ifcBuildingFromElement() {
    }

    @Test
    public void ifcSiteFromElement() {
    }

    @Test
    public void ifcPropertySetFromElement() {
    }

    @Test
    public void getPropertySetByName() {
    }

    @Test
    public void getPropertySetByStartsWith() {
    }

    @Test
    public void getSingleValueByName() {
    }

    @Test
    public void getNominalTextValueFromSingleValue() {
    }

    @Test
    public void propertyExistsInPropertySet() {
    }
}
