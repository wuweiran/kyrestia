package clan.midnight.kyrestia.pvm;

import clan.midnight.kyrestia.model.Execution;
import clan.midnight.kyrestia.model.ExecutionPoint;
import clan.midnight.kyrestia.model.Node;

import java.io.Serializable;
import java.util.*;

public class PvmExecutionPointView implements ExecutionPoint {
    private final Execution execution;
    private final ExecutionPoint supEp;
    private final List<ExecutionPoint> subEps;
    private final Map<String, Serializable> localContext;
    private final ExecutionPoint.Status status;
    private final Node currentNode;
    private final ExecutionPoint.NodeStage currentNodeStage;
    private final String waitEvent;
    private final boolean waitBereaved;

    static PvmExecutionPointView snapshot(PvmExecution execution) {
        return new PvmExecutionPointView(null, execution.mainEp);
    }

    private PvmExecutionPointView(ExecutionPoint supEp, PvmExecutionPoint ep) {
        this.execution = ep.execution;
        this.supEp = supEp;
        this.localContext = ep.rc.localContext;
        this.status = ep.rc.status;
        this.currentNode = ep.rc.currentNode;
        this.currentNodeStage = ep.rc.currentNodeStage;
        this.waitEvent = ep.rc.waitEvent;
        this.waitBereaved = ep.rc.waitBereaved;
        if (ep.getSubEps().isEmpty()) this.subEps = Collections.emptyList();
        else {
            this.subEps = new ArrayList<>();
            ep.getSubEps().forEach(subEp -> this.subEps.add(new PvmExecutionPointView(this, subEp)));
        }
    }

    @Override
    public Execution getExecution() {
        return execution;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public Node getCurrentNode() {
        return currentNode;
    }

    @Override
    public NodeStage getCurrentNodeStage() {
        return currentNodeStage;
    }

    @Override
    public ExecutionPoint getSupExecutionPoint() {
        return supEp;
    }

    @Override
    public Collection<ExecutionPoint> getSubExecutionPoints() {
        return subEps;
    }

    @Override
    public Map<String, Serializable> getLocalContext() {
        return localContext;
    }

    @Override
    public String getWaitEvent() {
        return waitEvent;
    }

    @Override
    public boolean isWaitingBereaved() {
        return waitBereaved;
    }
}
