package clan.midnight.kyrestia.pvm;

import clan.midnight.kyrestia.model.Node;
import clan.midnight.kyrestia.model.RuntimeExecutionPoint;

import java.util.UUID;

class ShatterNode implements Node {
    private final Node[] nodes;

    ShatterNode(Node... nodes) {
        this.nodes = nodes;
    }

    @Override
    public String id() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void enter(RuntimeExecutionPoint executionPoint) {
        // do nothing
    }

    @Override
    public void execute(RuntimeExecutionPoint executionPoint) {
        // do nothing
    }

    @Override
    public Node leave(RuntimeExecutionPoint executionPoint) {
        for (Node node : nodes) {
            executionPoint.newExecutionPointOn(node);
        }
        return null;
    }
}
