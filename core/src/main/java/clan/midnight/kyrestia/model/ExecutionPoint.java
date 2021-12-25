package clan.midnight.kyrestia.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public interface ExecutionPoint {
    Execution getExecution();

    enum Status {RUNNABLE, WAITING, TERMINATED}
    Status getStatus();

    Node getCurrentNode();

    enum NodeStage {ENTERING, EXECUTING, LEAVING}
    NodeStage getCurrentNodeStage();

    ExecutionPoint getSupExecutionPoint();

    Collection<ExecutionPoint> getSubExecutionPoints();

    Map<String, Serializable> getLocalContext();

    String getWaitEvent();

    boolean isWaitingBereaved();
}
