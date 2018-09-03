package se.bimsolution.db;

import java.util.List;

public interface Repository {
    void writeAllFails(List<Fail> fails);
    void writeRun(Run run);
}
