package se.bimsolution;

import org.bimserver.client.BimServerClient;
import org.bimserver.emf.IfcModelInterface;
import org.bimserver.shared.UsernamePasswordAuthenticationInfo;
import org.bimserver.shared.exceptions.ServerException;
import org.bimserver.shared.exceptions.UserException;
import se.bimsolution.db.PostgresRepository;
import se.bimsolution.db.Repository;
import se.bimsolution.query.*;

/**
 * Handles the queries to the BIMServer
 */
public class Main {
    public static void main(String[] args)  {
        Repository repo = null;
        try {
            BimServerClient bsc = new ClientBuilder(new UsernamePasswordAuthenticationInfo(args[0], args[1]), "http://104.248.40.190:8080/bimserver").build();
            new Listener(bsc, repo, 60000).listen();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            repo.close();
        }

    }
}
