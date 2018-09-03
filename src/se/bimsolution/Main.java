package se.bimsolution;

import org.bimserver.client.BimServerClient;
import org.bimserver.client.json.JsonBimServerClientFactory;
import org.bimserver.emf.IfcModelInterface;
import org.bimserver.interfaces.objects.SProject;
import org.bimserver.models.ifc2x3tc1.*;
import org.bimserver.shared.UsernamePasswordAuthenticationInfo;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EStructuralFeature;

import java.util.List;

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

//
//                List<IfcDoor> alldoors = modelInterface.getAllWithSubTypes(IfcDoor.class);
//                (IfcPropertySet) ((IfcRelDefinesByProperties)alldoors.get(0).getIsDefinedBy().get(1)).getRelatingPropertyDefinition();


                IfcPropertySet ps = (IfcPropertySet) modelInterface.getByGuid("0ZqyEYcjD1Q980drTUCHyH");
                System.out.println(ps);
                IfcPropertySingleValue ifcSingle =  (IfcPropertySingleValue) ps.getHasProperties().get(7);
                IfcValue value =  ifcSingle.getNominalValue();

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
