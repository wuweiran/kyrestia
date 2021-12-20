package clan.midnight.kyrestia.pvm;

import clan.midnight.kyrestia.model.Node;
import clan.midnight.kyrestia.model.Process;

public class SingleThreadSyncExecution extends PvmExecution {
    public SingleThreadSyncExecution(Process process) {
        super(process);
    }

    @Override
    protected PvmExecutionPoint createMainExecutionPoint() {
        return new SingleThreadSyncExecutionPoint(this);
    }

    public static class SingleThreadSyncExecutionPoint extends PvmExecutionPoint {
        SingleThreadSyncExecutionPoint(PvmExecution execution) {
            super(execution);
        }

        SingleThreadSyncExecutionPoint(PvmExecutionPoint supEp, Node node) {
            super(supEp, node);
        }

        @Override
        PvmExecutionPoint newSubExecutionPoint(Node node) {
            return new SingleThreadSyncExecutionPoint(this, node);
        }

        @Override
        void drive() {
            run();
        }
    }
}
