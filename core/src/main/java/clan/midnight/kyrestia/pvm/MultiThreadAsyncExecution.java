package clan.midnight.kyrestia.pvm;

import clan.midnight.kyrestia.model.Node;
import clan.midnight.kyrestia.model.Process;

public class MultiThreadAsyncExecution extends AbstractExecution {

    public MultiThreadAsyncExecution(Process process) {
        super(process);
    }

    @Override
    protected AbstractExecutionPoint createMainExecutionPoint() {
        return new MultiThreadASyncExecutionPoint(this);
    }

    public static class MultiThreadASyncExecutionPoint extends AbstractExecutionPoint {
        private volatile Thread t;

        MultiThreadASyncExecutionPoint(AbstractExecution execution) {
            super(execution);
        }

        MultiThreadASyncExecutionPoint(AbstractExecutionPoint supEp, Node node) {
            super(supEp, node);
        }

        @Override
        AbstractExecutionPoint newSubExecutionPoint(Node node) {
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
