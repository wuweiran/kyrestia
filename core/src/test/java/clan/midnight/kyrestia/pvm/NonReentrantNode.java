package clan.midnight.kyrestia.pvm;

import clan.midnight.kyrestia.model.RuntimeExecutionPoint;
import clan.midnight.kyrestia.model.Node;

import java.util.UUID;

class NonReentrantNode implements Node {
    private final Node next;
    private boolean entered = false;
    private boolean executed = false;
    private boolean left = false;

    NonReentrantNode(Node next) {
        this.next = next;
    }

    @Override
    public String id() {
        return UUID.randomUUID().toString();
    }

    @Override
    public synchronized void enter(RuntimeExecutionPoint executionPoint) {
        if (entered) {
            throw new IllegalStateException();
        }

        entered = true;
    }

    @Override
    public synchronized void execute(RuntimeExecutionPoint executionPoint) {
        if (executed) {
            throw new IllegalStateException();
        }

        executed = true;
    }

    @Override
    public synchronized Node leave(RuntimeExecutionPoint executionPoint) {
        if (left) {
            throw new IllegalStateException();
        }

        left = true;
        return next;
    }

    public boolean isEntered() {
        return entered;
    }

    public boolean isExecuted() {
        return executed;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isTraversed() {
        return isEntered() && isExecuted() && isLeft();
    }
}
