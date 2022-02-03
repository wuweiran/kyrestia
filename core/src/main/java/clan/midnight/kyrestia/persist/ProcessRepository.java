package clan.midnight.kyrestia.persist;

import clan.midnight.kyrestia.model.Process;

public interface ProcessRepository {
    Process getById(String id);

    void save(Process process);
}
