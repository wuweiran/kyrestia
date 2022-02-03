package clan.midnight.kyrestia.pvm;

import clan.midnight.kyrestia.model.Node;
import clan.midnight.kyrestia.model.Process;

public class TestProcess implements Process {
    private final Node startNode;

    public TestProcess(Node startNode) {
        this.startNode = startNode;
    }

    @Override
    public String id() {
        return "test_process";
    }

    @Override
    public Node startNode() {
        return startNode;
    }

    @Override
    public Node node(String nodeId) {
        throw new UnsupportedOperationException();
    }
}
