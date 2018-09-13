package se.bimsolution.query;

import org.bimserver.client.BimServerClient;
import org.bimserver.emf.IfcModelInterface;
import org.bimserver.ifc.IfcModel;
import org.bimserver.interfaces.objects.SProject;
import org.bimserver.shared.exceptions.ServerException;
import org.bimserver.shared.exceptions.UserException;

public class ModelBuilder {

    private  SProject project;
    private BimServerClient client;
    private String modelName;

    private boolean INCLUDE_GEOMETRY = false;
    private boolean RECORD_CHANGES = false;

    public ModelBuilder(BimServerClient client, String modelName) {
        this.client = client;
        this.modelName = modelName;
    }

    public ModelBuilder(BimServerClient client, SProject project) {
        this.client = client;
        this.project = project;
    }

    /**Builds a model from the stored fields URL and authentication
     * This method can take quite some time...
     * If a project has been injected, it uses the project, else, it uses the name
     * @return A complete IFCModel
     */
    public IfcModelInterface build() throws ServerException, UserException {
        if (this.project == null) {
            SProject project = client.getServiceInterface().getProjectsByName(modelName).get(0);
            return client.getModel(project, project.getLastRevisionId(), true, false, INCLUDE_GEOMETRY);
        } else {
            return client.getModel(this.project, this.project.getLastRevisionId(), true, RECORD_CHANGES, INCLUDE_GEOMETRY);
        }
    }
}
