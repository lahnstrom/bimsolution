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
        List<Class> classList = getStandardClassList();
        Repository repo = null;

        try {
            BimServerClient bsc = new ClientBuilder(new UsernamePasswordAuthenticationInfo(args[0], args[1]), "http://104.248.40.190:8080/bimserver").build();
            IfcModelInterface model = null;
            model = new ModelBuilder(bsc, "A2-400").build();
            WrongStoreyChecker wrongStoreyChecker = new WrongStoreyChecker(model, repo, 1, QueryUtils.getStandardClassList());
            PropertySetMissingChecker propertySetMissingChecker = new PropertySetMissingChecker(model, repo, 1, QueryUtils.getStandardClassList());
            AreaChecker areaChecker = new AreaChecker(model, repo, 1, null);
            WrongBSABChecker wrongBSABChecker = new WrongBSABChecker(model, repo, 1, classList);
            propertySetMissingChecker.run();
            wrongStoreyChecker.run();
            areaChecker.run();
            wrongStoreyChecker.run();
            wrongBSABChecker.run();
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
