package clan.midnight.kyrestia.pvm;

import clan.midnight.kyrestia.model.Node;
import clan.midnight.kyrestia.model.Process;

public class MultiThreadAsyncExecution extends PvmExecution {

    public MultiThreadAsyncExecution(Process process) {
        super(process);
    }

    @Override
    protected PvmExecutionPoint createMainExecutionPoint() {
        return new MultiThreadASyncExecutionPoint(this);
    }

    public static class MultiThreadASyncExecutionPoint extends PvmExecutionPoint {
        private volatile Thread t;

        MultiThreadASyncExecutionPoint(PvmExecution execution) {
            super(execution);
        }

        MultiThreadASyncExecutionPoint(PvmExecutionPoint supEp, Node node) {
            super(supEp, node);
        }

        @Override
        PvmExecutionPoint newSubExecutionPoint(Node node) {
            return new MultiThreadASyncExecutionPoint(this, node);
        }

        @Override
        void drive() {
            if (t == null || !t.isAlive()) {
                synchronized (this) {
                    if (t == null || !t.isAlive()) {
                        t = new Thread(this::run);
                    }
                    t.start();
                }
            }
        }
    }
}
