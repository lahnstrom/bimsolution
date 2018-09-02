package se.bimsolution;

import org.bimserver.client.BimServerClient;
import org.bimserver.client.json.JsonBimServerClientFactory;
import org.bimserver.shared.UsernamePasswordAuthenticationInfo;
import org.bimserver.shared.exceptions.BimServerClientException;

/**
 * Handles the queries to the BIMServer
 */
public class Main {
    public static void main(String[] args) {
        try (JsonBimServerClientFactory factory = new JsonBimServerClientFactory("http://104.248.40.190:8080/bimserver")) {
            // Creating a client in a try statement, this makes sure the client will be closed after use
            try (BimServerClient client = factory.create(new UsernamePasswordAuthenticationInfo(args[0], args[1]))) {
                client.getServiceInterface().getAllProjects(false, false).forEach(x -> System.out.println(x.getName()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }
}
