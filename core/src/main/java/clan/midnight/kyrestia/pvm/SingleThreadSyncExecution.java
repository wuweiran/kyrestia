package clan.midnight.kyrestia.pvm;

import clan.midnight.kyrestia.model.Node;
import clan.midnight.kyrestia.model.Process;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

public class SingleThreadSyncExecution extends AbstractExecution {
    private Map<String, Serializable> parameters;

    SingleThreadSyncExecution(String id, Process process) {
        super(id, process);
        this.parameters = Collections.emptyMap();
    }

    SingleThreadSyncExecution(String id, Process process, Map<String, Serializable> parameters) {
        this(id, process);
        this.parameters = parameters;
    }

    SingleThreadSyncExecution(String id, Process process,
                                     AbstractExecutionPoint.MetaContainer metaContainer, Status status) {
        super(id, process, metaContainer, status);
        this.parameters = Collections.emptyMap();
    }

    @Override
    protected AbstractExecutionPoint newMainExecutionPoint() {
        return new SingleThreadSyncExecutionPoint(this, parameters);
    }

    @Override
    protected AbstractExecutionPoint newMainExecutionPoint(AbstractExecutionPoint.MetaContainer metaContainer) {
        return new SingleThreadSyncExecutionPoint(this, metaContainer);
    }

    public static class SingleThreadSyncExecutionPoint extends AbstractExecutionPoint {
        SingleThreadSyncExecutionPoint(AbstractExecution execution, Map<String, Serializable> localContext) {
            super(execution, localContext);
        }

        SingleThreadSyncExecutionPoint(AbstractExecutionPoint supEp, Node node) {
            super(supEp, node);
        }

        SingleThreadSyncExecutionPoint(AbstractExecution execution, MetaContainer metaContainer) {
            super(execution, metaContainer);
        }

        SingleThreadSyncExecutionPoint(AbstractExecutionPoint supEp, MetaContainer metaContainer) {
            super(supEp, metaContainer);
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
