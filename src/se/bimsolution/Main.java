package se.bimsolution;

import org.bimserver.client.BimServerClient;
import org.bimserver.emf.IfcModelInterface;
import org.bimserver.shared.UsernamePasswordAuthenticationInfo;
import org.bimserver.shared.exceptions.ServerException;
import org.bimserver.shared.exceptions.UserException;
import se.bimsolution.db.PostgresRepository;
import se.bimsolution.db.Repository;
import se.bimsolution.query.*;

//import se.bimsolution.query.machine.IdValidationMachine;

import se.bimsolution.query.machine.*;
//import se.bimsolution.query.machine.mockQueryMachine;


import java.sql.SQLException;
import java.util.List;
import static se.bimsolution.query.QueryUtils.*;
/**
 * Handles the queries to the BIMServer
 */
public class Main {
    public static void main(String[] args)  {
        Repository repo = null;
        try {
            BimServerClient bsc = new ClientBuilder(new UsernamePasswordAuthenticationInfo(args[0], args[1]), "http://104.248.40.190:8080/bimserver").build();
            new Listener(bsc, repo, 60000).listen();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (UserException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            repo.close();
        }

    }
}
