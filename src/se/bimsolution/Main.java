package se.bimsolution;

import org.bimserver.client.BimServerClient;
import org.bimserver.client.json.JsonBimServerClientFactory;
import org.bimserver.emf.IfcModelInterface;
import org.bimserver.ifc.IfcModel;
import org.bimserver.interfaces.objects.SProject;
import org.bimserver.models.ifc2x3tc1.*;
import org.bimserver.shared.QueryContext;
import org.bimserver.shared.UsernamePasswordAuthenticationInfo;
import org.bimserver.shared.exceptions.ServerException;
import org.bimserver.shared.exceptions.UserException;
import org.eclipse.emf.common.util.EList;
import se.bimsolution.db.Fail;
import se.bimsolution.db.PostgresRepository;
import se.bimsolution.db.Repository;
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
    public static void main(String[] args)  {

        Repository repo = null;


        try {
            BimServerClient bsc = new ClientBuilder(new UsernamePasswordAuthenticationInfo(args[0], args[1]), "http://104.248.40.190:8080/bimserver").build();
            IfcModelInterface model = null;
            model = new ModelBuilder(bsc, "A2-400").build();
            QueryCoordinator qc = new QueryCoordinator(30, repo, model);
            qc.run();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (UserException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        repo.close();






    }
}
