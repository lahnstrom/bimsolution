package se.bimsolution.query;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class QueryUtilsTest {
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(QueryUtils.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void ifcBuildingStoreyFromElement() {
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
