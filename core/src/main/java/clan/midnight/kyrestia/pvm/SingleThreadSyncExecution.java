package clan.midnight.kyrestia.pvm;

import clan.midnight.kyrestia.model.Node;
import clan.midnight.kyrestia.model.Process;

public class SingleThreadSyncExecution extends AbstractExecution {
    public SingleThreadSyncExecution(Process process) {
        super(process);
    }

    @Override
    protected AbstractExecutionPoint newMainExecutionPoint() {
        return new SingleThreadSyncExecutionPoint(this);
    }

    public static class SingleThreadSyncExecutionPoint extends AbstractExecutionPoint {
        SingleThreadSyncExecutionPoint(AbstractExecution execution) {
            super(execution);
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
