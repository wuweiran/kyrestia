package clan.midnight.kyrestia.pvm;

import clan.midnight.kyrestia.model.Execution;
import clan.midnight.kyrestia.model.ExecutionPoint;
import clan.midnight.kyrestia.model.Node;

import java.io.Serializable;
import java.util.*;

public class SnapshotExecutionPoint implements ExecutionPoint {
    private final Execution execution;
    private final ExecutionPoint supEp;
    private final List<ExecutionPoint> subEps;
    private final Map<String, Serializable> localContext;
    private final ExecutionPoint.Status status;
    private final Node currentNode;
    private final ExecutionPoint.NodeStage currentNodeStage;
    private final String waitEvent;
    private final boolean waitBereaved;

    static SnapshotExecutionPoint snapshot(AbstractExecution execution) {
        return new SnapshotExecutionPoint(null, execution.mainEp);
    }

    private SnapshotExecutionPoint(ExecutionPoint supEp, AbstractExecutionPoint ep) {
        this.execution = ep.execution;
        this.supEp = supEp;
        AbstractExecutionPoint.MetaContainer rc = ep.rc;
        this.localContext = rc.localContext;
        this.status = rc.status;
        this.currentNode = rc.currentNode;
        this.currentNodeStage = rc.currentNodeStage;
        this.waitEvent = rc.waitEvent;
        this.waitBereaved = rc.waitBereaved;
        if (rc.subEps == null || rc.subEps.isEmpty()) this.subEps = Collections.emptyList();
        else {
            this.subEps = new ArrayList<>();
            rc.subEps.forEach(subEp -> this.subEps.add(new SnapshotExecutionPoint(this, subEp)));
        }
    }

    @Override
    public Execution execution() {
        return execution;
    }

    @Override
    public Status status() {
        return status;
    }

    @Override
    public Node currentNode() {
        return currentNode;
    }

    @Override
    public NodeStage currentNodeStage() {
        return currentNodeStage;
    }

    @Override
    public ExecutionPoint supExecutionPoint() {
        return supEp;
    }

    @Override
    public Collection<ExecutionPoint> subExecutionPoints() {
        return subEps;
    }

    @Override
    public Map<String, Serializable> localContext() {
        return localContext;
    }

    @Override
    public String waitEvent() {
        return waitEvent;
    }

    @Override
    public boolean isWaitingBereaved() {
        return waitBereaved;
    }
}
