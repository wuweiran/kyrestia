package clan.midnight.kyrestia.persist;

import clan.midnight.kyrestia.infra.spi.Extension;
import clan.midnight.kyrestia.model.Process;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Extension(group = PersistenceHelper.PERSISTENCE_EXTENSION_GROUP)
public class MemoryProcessRepository implements ProcessRepository {
    private final ConcurrentMap<String, Process> processes = new ConcurrentHashMap<>(32);

    @Override
    public Process getById(String id) {
        return processes.get(id);
    }

    @Override
    public void save(Process process) {
        processes.putIfAbsent(process.id(), process);
    }
}
