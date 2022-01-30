package clan.midnight.kyrestia.pvm;

import clan.midnight.kyrestia.config.Configuration;
import clan.midnight.kyrestia.model.Execution;
import clan.midnight.kyrestia.model.Process;

import java.io.Serializable;
import java.util.Map;

public class ExecutionFactory {
    private ExecutionFactory() {}

    public static Execution createExecution(Process process) {
        String id = Configuration.idGenerator.generate();
        if (Configuration.multiThreadExecution) {
            return new MultiThreadAsyncExecution(id, process, Configuration.executorService);
        }
        return new SingleThreadSyncExecution(id, process);
    }

    public static Execution createExecution(Process process, Map<String, Serializable> parameters) {
        if (parameters == null || parameters.isEmpty()) {
            return createExecution(process);
        }

        String id = Configuration.idGenerator.generate();
        if (Configuration.multiThreadExecution) {
            return new MultiThreadAsyncExecution(id, process, Configuration.executorService, parameters);
        }
        return new SingleThreadSyncExecution(id, process, parameters);
    }
}
