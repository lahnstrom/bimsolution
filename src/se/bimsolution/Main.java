package se.bimsolution;

import org.bimserver.client.BimServerClient;
import org.bimserver.client.json.JsonBimServerClientFactory;
import org.bimserver.emf.IfcModelInterface;
import org.bimserver.interfaces.objects.SProject;
import org.bimserver.models.ifc2x3tc1.*;
import org.bimserver.shared.QueryContext;
import org.bimserver.shared.UsernamePasswordAuthenticationInfo;
import org.eclipse.emf.common.util.EList;
import se.bimsolution.db.Fail;
import se.bimsolution.db.PostgresRepository;
import se.bimsolution.query.ClientBuilder;
import se.bimsolution.query.ModelBuilder;
import se.bimsolution.query.QueryCoordinator;

import se.bimsolution.query.QueryUtils;
//import se.bimsolution.query.machine.IdValidationMachine;

import se.bimsolution.query.machine.QueryMachine;
import se.bimsolution.query.machine.mockQueryMachine;
//import se.bimsolution.query.machine.mockQueryMachine;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Handles the queries to the BIMServer
 */
public class Main {
    public static void main(String[] args) {




        try {
            BimServerClient bsc = new ClientBuilder(new UsernamePasswordAuthenticationInfo(args[0], args[1]),
                    "http://104.248.40.190:8080/bimserver").build();
            IfcModelInterface model = new ModelBuilder(bsc, "A2-400").build();
            System.out.println(QueryUtils.extractNameFromClass(model.getAll(IfcDoor.class).get(0).getClass()));
            PostgresRepository postgresRepository = new PostgresRepository(args[2],
                    args[3], args[4]);

            new QueryCoordinator(postgresRepository, new mockQueryMachine()).run();


            IfcDoor door = model.getAll(IfcDoor.class).get(0);
            System.out.println(QueryUtils.getAbsoluteZValue(door));
            System.out.println(QueryUtils.getAbsoluteZValue(QueryUtils.ifcBuildingStoreyFromElement(door)));
            for (Class clazz :
                    QueryUtils.standardClassList()) {
                for (Object obj :
                        model.getAll(clazz)) {
                    if (obj instanceof IfcElement) {
                        try {

                            if (QueryUtils.elementIsBelowFloorLevel((IfcElement) obj, 0.001)) {
                                System.out.println(((IfcElement) obj).getName() + "   OID: " + ((IfcObject) obj).getOid());
                                System.out.println("Difference: " + QueryUtils.getHeightDifferenceBetweenStoreyAndElement((IfcElement) obj));
                            }
                        } catch (Exception ignored) {
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


/*

    public static void main(String[] args) throws InterruptedException {
        String[] strings = IfcDoor.class.toGenericString().split("\\.");
        Arrays.asList(strings).forEach(System.out::println);
        String last = strings[strings.length-1];
        System.out.println(last);

        try (JsonBimServerClientFactory factory = new JsonBimServerClientFactory("http://104.248.40.190:8080/bimserver")) {
            // Creating a client in a try statement, this makes sure the client will be closed after use
            try (BimServerClient client = factory.create(new UsernamePasswordAuthenticationInfo(args[0], args[1]))) {
                //Hämta projektet med namnet A2-400
                SProject project = client.getServiceInterface().getProjectsByName("A2-400").get(0);
                //Ladda Modellen, hela på en gångtr
                IfcModelInterface modelInterface = client.getModel(project, project.getLastRevisionId(), true, false, false);
                System.out.println("Im here: " + modelInterface);
//              //Hitta alla dörrar!
                List<IfcDoor> alldoors = modelInterface.getAllWithSubTypes(IfcDoor.class);
                //Ta den första dörren
                IfcDoor ifcDoor = alldoors.get(0);
                //Kolla vad dörren är definierat av
                EList<IfcRelDefines> ifcRelDefinesEList = ifcDoor.getIsDefinedBy();
                IfcPropertySet ps = null;

                //Hitta den första definitionen som är ett propertySet
                for (IfcRelDefines def:
                     ifcRelDefinesEList) {
                    //Är relationen en defines by property?
                    if (def instanceof IfcRelDefinesByProperties ) {
                        //Går den att casta till ett propertyset?
                        try {
                            ps = (IfcPropertySet) ((IfcRelDefinesByProperties) def).getRelatingPropertyDefinition();
                            break;
                        } catch (Exception ignored) { }
                    }
                }
//                IfcPropertySetDefinition ifcps = (((IfcRelDefinesByProperties)ifcDoor.getIsDefinedBy().get(0)).getRelatingPropertyDefinition());
                //Gå igenom alla properties i propertysetet
                for (Object obj:
                     ps.getHasProperties()) {
                    //Det är sannolikt single values...
                    IfcPropertySingleValue ifcSingle =  (IfcPropertySingleValue) obj;
                    //Plocka ut nominal value
                    IfcValue value =  ifcSingle.getNominalValue();

                    String stringv = "";

                    //Ta ut textvärdet om det finns
                    if (value instanceof IfcText) {
                        stringv = ((IfcText)value).getWrappedValue();
                    }

                    System.out.println(ifcSingle.getName() + " " + stringv);
                }



//                IfcPropertySingleValue ifcSingle =  (IfcPropertySingleValue) ps.getHasProperties().get(7);
//                IfcValue value =  ifcSingle.getNominalValue();
//                String stringv = "";
//
//                System.out.println(ifcSingle.getName());
//                if (value instanceof IfcText) {
//                    stringv = ((IfcText)value).getWrappedValue();
//                }
//
//                System.out.println(stringv);
//                HashSet<
//               client.getServiceInterface().download()
//                SDataObject sDataObject = client.getLowLevelInterface().getDataObjectByOid(project.getLastRevisionId(), 393834L);
//                sDataObject.getValues().forEach(x-> System.out.println(x));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
*/

    }
}
