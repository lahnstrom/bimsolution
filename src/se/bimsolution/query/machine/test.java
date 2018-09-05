package se.bimsolution.query.machine;

import org.bimserver.client.BimServerClient;
import org.bimserver.emf.IfcModelInterface;
import org.bimserver.shared.UsernamePasswordAuthenticationInfo;
import se.bimsolution.query.ClientBuilder;
import se.bimsolution.query.ModelBuilder;
import se.bimsolution.query.QueryUtils;

public class test {
    public static void main(String[] args) throws Exception {
        BimServerClient bsc = new ClientBuilder(new UsernamePasswordAuthenticationInfo(args[0], args[1]),
                "http://104.248.40.190:8080/bimserver").build();
        IfcModelInterface model = new ModelBuilder(bsc, "A2-400").build();
        QueryMachine qm = new IdValidationMachine(model, 1,1);
        qm.run();
        System.out.println(qm.getCount() + " " + qm.getFailCount());
        bsc.close();
    }
}
