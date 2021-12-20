package clan.midnight.kyrestia.pvm;

import clan.midnight.kyrestia.model.Node;
import clan.midnight.kyrestia.model.RuntimeExecutionPoint;

class BlockOnExecutingNrNode extends NonReentrantNode {
    private final String event;

    BlockOnExecutingNrNode(String event, Node next) {
        super(next);
        this.event = event;
    }

    @Override
    public synchronized void execute(RuntimeExecutionPoint executionPoint) {
        super.execute(executionPoint);
        executionPoint.await(event);
    }
}
