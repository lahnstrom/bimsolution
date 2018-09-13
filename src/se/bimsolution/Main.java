package se.bimsolution;

import org.bimserver.client.BimServerClient;
import org.bimserver.emf.IfcModelInterface;
import org.bimserver.shared.UsernamePasswordAuthenticationInfo;
import org.bimserver.shared.exceptions.ServerException;
import org.bimserver.shared.exceptions.UserException;
import se.bimsolution.db.PostgresRepository;
import se.bimsolution.db.Repository;
import se.bimsolution.query.*;
import se.bimsolution.query.machine.WrongBSABChecker;

/**
 * Handles the queries to the BIMServer
 */
public class Main {
    public static void main(String[] args)  {
        Repository repo = null;
        try {
            BimServerClient bsc = new ClientBuilder(new UsernamePasswordAuthenticationInfo(args[0], args[1]), "http://104.248.40.190:8080/bimserver").build();
//            new WrongBSABChecker(new ModelBuilder(bsc, "A2-400-V").build(), repo, 47, QueryUtils.getElementClassList()).run();
            new Listener(bsc, repo, 300000).listen();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert repo != null;
            repo.close();
        }

    }
}
