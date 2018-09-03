package se.bimsolution;

import org.bimserver.client.BimServerClient;
import org.bimserver.client.json.JsonBimServerClientFactory;
import org.bimserver.emf.IfcModelInterface;
import org.bimserver.interfaces.objects.SDataObject;
import org.bimserver.interfaces.objects.SProject;
import org.bimserver.models.ifc4.IfcDoor;
import org.bimserver.models.ifc4.IfcDoorStandardCase;
import org.bimserver.models.ifc4.IfcRelDefinesByProperties;
import org.bimserver.shared.UsernamePasswordAuthenticationInfo;
import org.bimserver.shared.exceptions.BimServerClientException;
import org.bimserver.shared.meta.SClass;
import org.bimserver.shared.meta.SField;

/**
 * Handles the queries to the BIMServer
 */
public class Main {
    public static void main(String[] args) {
        try (JsonBimServerClientFactory factory = new JsonBimServerClientFactory("http://104.248.40.190:8080/bimserver")) {
            // Creating a client in a try statement, this makes sure the client will be closed after use
            try (BimServerClient client = factory.create(new UsernamePasswordAuthenticationInfo(args[0], args[1]))) {
                SProject project = client.getServiceInterface().getProjectsByName("A2-400").get(0);
                System.out.println(project.getOid());
                IfcModelInterface modelInterface = client.getModel(project, project.getLastRevisionId(), true, false, false);
                System.out.println("Im here: " + modelInterface);

                System.out.println(modelInterface);
//
                modelInterface.getAllWithSubTypes(IfcDoor.class);
//                HashSet<>
//               client.getServiceInterface().download()
//                SDataObject sDataObject = client.getLowLevelInterface().getDataObjectByOid(project.getLastRevisionId(), 393834L);
//                sDataObject.getValues().forEach(x-> System.out.println(x));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }
}
