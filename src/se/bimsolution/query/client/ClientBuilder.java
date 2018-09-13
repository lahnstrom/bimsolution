package se.bimsolution.query.client;

import org.bimserver.client.BimServerClient;
import org.bimserver.client.json.JsonBimServerClientFactory;
import org.bimserver.shared.UsernamePasswordAuthenticationInfo;
import org.bimserver.shared.exceptions.BimServerClientException;

import java.net.PasswordAuthentication;

public class ClientBuilder {

    private UsernamePasswordAuthenticationInfo auth;
    private String address;

    public ClientBuilder(UsernamePasswordAuthenticationInfo auth, String address) {
        this.auth = auth;
        this.address = address;
    }


    /**
     * Uses the authentication and address provided and connects to the BIMServer.
     *
     * @return A working BimServerClient
     * @throws Exception when unable to connect, perhaps check credentials
     */
    public BimServerClient build() throws Exception {
        JsonBimServerClientFactory factory = new JsonBimServerClientFactory("http://104.248.40.190:8080/bimserver");
        try (BimServerClient client = factory.create(auth)) {
            return client;
        }
    }
}
