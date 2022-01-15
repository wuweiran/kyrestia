package clan.midnight.kyrestia.pvm;

import clan.midnight.kyrestia.config.Configuration;
import clan.midnight.kyrestia.model.Execution;
import clan.midnight.kyrestia.model.Process;

public class ExecutionFactory {
    private ExecutionFactory() {}

    public static Execution createExecution(Process process) {
        if (Configuration.multiThreadExecution) {
            return new MultiThreadAsyncExecution(process, Configuration.executorService);
        }

        return new SingleThreadSyncExecution(process);
    }
}
