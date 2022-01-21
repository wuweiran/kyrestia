package clan.midnight.kyrestia.pvm;

import clan.midnight.kyrestia.config.Configuration;
import clan.midnight.kyrestia.model.Execution;
import clan.midnight.kyrestia.model.Process;

public class ExecutionFactory {
    private ExecutionFactory() {}

    public static Execution createExecution(Process process) {
        String id = Configuration.idGenerator.generate();
        if (Configuration.multiThreadExecution) {
            return new MultiThreadAsyncExecution(id, process, Configuration.executorService);
        }
        return new SingleThreadSyncExecution(id, process);
    }
}
