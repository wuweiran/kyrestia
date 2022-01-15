package clan.midnight.kyrestia.pvm;

import clan.midnight.kyrestia.model.Node;
import clan.midnight.kyrestia.model.Process;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class MultiThreadAsyncExecution extends AbstractExecution {
    private final ExecutorService executorService;

    public MultiThreadAsyncExecution(Process process, ExecutorService executorService) {
        super(process);
        this.executorService = executorService;
    }

    @Override
    protected AbstractExecutionPoint newMainExecutionPoint() {
        return new MultiThreadASyncExecutionPoint(this, executorService);
    }

    public static class MultiThreadASyncExecutionPoint extends AbstractExecutionPoint {
        private final ExecutorService executorService;
        private volatile Future<?> future;

        MultiThreadASyncExecutionPoint(AbstractExecution execution, ExecutorService executorService) {
            super(execution);
            this.executorService = executorService;
        }

        MultiThreadASyncExecutionPoint(AbstractExecutionPoint supEp, Node node, ExecutorService executorService) {
            super(supEp, node);
            this.executorService = executorService;
        }

        @Override
        AbstractExecutionPoint newSubExecutionPoint(Node node) {
            return new MultiThreadASyncExecutionPoint(this, node, executorService);
        }

        @Override
        void drive() {
            if (future == null || (future.isCancelled() || !future.isDone())) {
                synchronized (this) {
                    if (future == null || (future.isCancelled() || !future.isDone())) {
                        future = executorService.submit(this::run);
                    }
                }
            }
        }
    }
}
