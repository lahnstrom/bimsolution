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

    }
}
