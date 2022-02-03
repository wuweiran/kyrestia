package clan.midnight.kyrestia.persist;

import clan.midnight.kyrestia.model.Execution;

public interface ExecutionRepository {
    Execution getById(String id);

    void save(Execution execution);
}
