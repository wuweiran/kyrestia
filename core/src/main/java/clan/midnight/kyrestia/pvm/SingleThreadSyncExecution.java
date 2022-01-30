package clan.midnight.kyrestia.pvm;

import clan.midnight.kyrestia.model.Node;
import clan.midnight.kyrestia.model.Process;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

public class SingleThreadSyncExecution extends AbstractExecution {
    private Map<String, Serializable> parameters;

    public SingleThreadSyncExecution(String id, Process process) {
        super(id, process);
        this.parameters = Collections.emptyMap();
    }

    public SingleThreadSyncExecution(String id, Process process, Map<String, Serializable> parameters) {
        this(id, process);
        this.parameters = parameters;
    }

    @Override
    protected AbstractExecutionPoint newMainExecutionPoint() {
        return new SingleThreadSyncExecutionPoint(this, parameters);
    }

    public static class SingleThreadSyncExecutionPoint extends AbstractExecutionPoint {
        SingleThreadSyncExecutionPoint(AbstractExecution execution, Map<String, Serializable> localContext) {
            super(execution, localContext);
        }

        SingleThreadSyncExecutionPoint(AbstractExecutionPoint supEp, Node node) {
            super(supEp, node);
        }

        @Override
        AbstractExecutionPoint newSubExecutionPoint(Node node) {
            return new SingleThreadSyncExecutionPoint(this, node);
        }

        @Override
        void drive() {
            run();
        }
    }
}
