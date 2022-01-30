package clan.midnight.kyrestia.pvm;

import clan.midnight.kyrestia.model.Node;
import clan.midnight.kyrestia.model.Process;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class MultiThreadAsyncExecution extends AbstractExecution {
    private final ExecutorService executorService;
    private Map<String, Serializable> parameters;

    public MultiThreadAsyncExecution(String id, Process process, ExecutorService executorService) {
        super(id, process);
        this.executorService = executorService;
        this.parameters = Collections.emptyMap();
    }

    public MultiThreadAsyncExecution(String id, Process process, ExecutorService executorService,
                                     Map<String, Serializable> parameters) {
        this(id, process, executorService);
        this.parameters = parameters;
    }

    @Override
    protected AbstractExecutionPoint newMainExecutionPoint() {
        return new MultiThreadASyncExecutionPoint(this, executorService, parameters);
    }

    public static class MultiThreadASyncExecutionPoint extends AbstractExecutionPoint {
        private final ExecutorService executorService;
        private volatile Future<?> future;

        MultiThreadASyncExecutionPoint(AbstractExecution execution, ExecutorService executorService,
                                       Map<String, Serializable> localContext) {
            super(execution, localContext);
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
