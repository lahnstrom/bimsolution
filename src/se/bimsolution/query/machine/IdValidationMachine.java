package se.bimsolution.query.machine;

import org.bimserver.emf.IfcModelInterface;
import org.bimserver.models.ifc2x3tc1.IfcDoor;
import se.bimsolution.db.Fail;

import java.util.List;

public class IdValidationMachine implements QueryMachine {

    private IfcModelInterface model;
    private List<Fail> fails;
    private int count;
    private int failCount;
    private String error;
    private boolean hasRun;
    private List<Class> classList;



    @Override
    public List<Fail> getFails() {
        if (!hasRun) {
            throw new IllegalStateException("The machine must run before calling the getFails method.");
        } else if (error != null) {
            throw new IllegalStateException("The machine's run method resulted in an error being thrown. " +
                    "Therefore this method can't be called");
        }
        return null;
    }

    @Override
    public int getCount() {
        if (!hasRun) {
            throw new IllegalStateException("The machine must run before calling the getCount method.");
        } else if (error != null) {
            throw new IllegalStateException("The machine's run method resulted in an error being thrown. " +
                    "Therefore this method can't be called");
        }
        return 0;
    }

    @Override
    public int getFailCount() {
        if (!hasRun) {
            throw new IllegalStateException("The machine must run before calling the getFailCount method.");
        } else if (error != null) {
            throw new IllegalStateException("The machine's run method resulted in an error being thrown. " +
                    "Therefore this method can't be called");
        }
        return this.failCount;
    }

    @Override
    public String getError() {
        if (!hasRun) {
            throw new IllegalStateException("The machine must run before calling the getError method.");
        }
        return this.error;
    }

    private void populateClassList() {
        //TODO This method should populate the list of classes
        classList.add(IfcDoor.class);
    }

    @Override
    public void run() {

        hasRun = true;
    }
}
