package clan.midnight.kyrestia.pvm;

import clan.midnight.kyrestia.model.Execution;
import clan.midnight.kyrestia.model.ExecutionPoint;
import clan.midnight.kyrestia.model.RuntimeExecutionPoint;
import clan.midnight.kyrestia.model.Node;

import java.io.Serializable;
import java.util.*;

public abstract class AbstractExecutionPoint implements RuntimeExecutionPoint {
    protected final AbstractExecution execution;
    protected final AbstractExecutionPoint supEp;
    protected volatile MetaContainer rc;
    protected volatile MetaContainer uc;

    static class MetaContainer {
        ArrayList<AbstractExecutionPoint> subEps;
        Map<String, Serializable> localContext;
        ExecutionPoint.Status status;
        Node currentNode;
        ExecutionPoint.NodeStage currentNodeStage;
        String waitEvent;
        boolean waitBereaved;

        MetaContainer(List<AbstractExecutionPoint> subEps, Map<String, Serializable> localContext,
                      ExecutionPoint.Status status, Node currentNode, ExecutionPoint.NodeStage currentNodeStage,
                      String waitEvent, boolean waitBereaved) {
            this.subEps = subEps == null ? null : new ArrayList<>(subEps);
            this.localContext = localContext == null ? null : new HashMap<>(localContext);
            this.status = status;
            this.currentNode = currentNode;
            this.currentNodeStage = currentNodeStage;
            this.waitEvent = waitEvent;
            this.waitBereaved = waitBereaved;
        }

        MetaContainer(MetaContainer rt) {
            this(rt.subEps, rt.localContext, rt.status, rt.currentNode, rt.currentNodeStage, rt.waitEvent, rt.waitBereaved);
        }
    }

    protected AbstractExecutionPoint(AbstractExecution execution) {
        this.execution = execution;
        this.supEp = null;
        this.rc = new MetaContainer(null, null, ExecutionPoint.Status.RUNNABLE,
                execution.getProcess().getStartNode(), ExecutionPoint.NodeStage.ENTERING,
                null, false);
    }

    protected AbstractExecutionPoint(AbstractExecutionPoint supEp, Node node) {
        this.supEp = supEp;
        this.execution = supEp.execution;
        this.rc = new MetaContainer(null, null, ExecutionPoint.Status.RUNNABLE,
                node, ExecutionPoint.NodeStage.ENTERING,
                null, false);
    }

    Collection<AbstractExecutionPoint> getSubEps() {
        return rc.subEps == null ? Collections.emptyList() : rc.subEps;
    }

    @Override
    public void putContext(String key, Serializable value) {
        uc.localContext.put(key, value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Serializable> T getContext(String key) {
        if (rc.localContext != null && rc.localContext.containsKey(key)) {
            return (T) rc.localContext.get(key);
        }
        if (supEp == null) return null;
        return supEp.getContext(key);
    }

    @Override
    public RuntimeExecutionPoint newExecutionPointOn(Node node) {
        AbstractExecutionPoint newSubEp = newSubExecutionPoint(node);
        if (uc.subEps == null) uc.subEps = new ArrayList<>();
        uc.subEps.add(newSubEp);
        return newSubEp;
    }

    @Override
    public synchronized void await(String event) {
        uc.status = ExecutionPoint.Status.WAITING;
        uc.waitEvent = event;
    }

    @Override
    public synchronized void awaitBereaved() {
        uc.status = ExecutionPoint.Status.WAITING;
        uc.waitEvent = null;
        uc.waitBereaved = true;
    }

    protected synchronized void run() {
        while (execution.getStatus() != Execution.Status.SUSPEND && rc.status == ExecutionPoint.Status.RUNNABLE) {
            uc = new MetaContainer(rc);
            switch (rc.currentNodeStage) {
                case ENTERING:
                    rc.currentNode.enter(this);
                    uc.currentNodeStage = ExecutionPoint.NodeStage.EXECUTING;
                    break;
                case EXECUTING:
                    rc.currentNode.execute(this);
                    uc.currentNodeStage = ExecutionPoint.NodeStage.LEAVING;
                    break;
                case LEAVING:
                    uc.currentNode = rc.currentNode.leave(this);
                    if (uc.currentNode == null) {
                        uc.currentNode = null;
                        uc.currentNodeStage = null;
                        uc.status = ExecutionPoint.Status.TERMINATED;
                    } else uc.currentNodeStage = ExecutionPoint.NodeStage.ENTERING;
                    break;
                default:
            }
            boolean newNodeAdded = uc.subEps != null && uc.subEps.size() > getSubEps().size();
            rc = uc;
            uc = null;
            if (newNodeAdded) {
                for (AbstractExecutionPoint subEp : getSubEps()) subEp.drive();
            }
        }

        tryPopTermination();
    }

    void tryPopTermination() {
        if (rc.status == ExecutionPoint.Status.TERMINATED) {
            boolean allTerminated = true;
            for (AbstractExecutionPoint subEp : getSubEps()) {
                if (subEp.rc.status != ExecutionPoint.Status.TERMINATED) {
                    allTerminated = false;
                    break;
                }
            }
            if (allTerminated) {
                if (supEp != null) supEp.tryPopTermination();
                else execution.markTerminated();
            }
        } else if (rc.status == ExecutionPoint.Status.WAITING && rc.waitBereaved) {
            rc.status = ExecutionPoint.Status.RUNNABLE;
            rc.waitBereaved = false;
            drive();
        }
    }

    void signal(String event) {
        if (rc.status == ExecutionPoint.Status.WAITING && rc.waitEvent != null && rc.waitEvent.equals(event)) {
            rc.status = ExecutionPoint.Status.RUNNABLE;
            rc.waitEvent = null;
            drive();
        }

        for (AbstractExecutionPoint subEp : getSubEps()) subEp.signal(event);
    }

    abstract AbstractExecutionPoint newSubExecutionPoint(Node node);

    abstract void drive();
}
