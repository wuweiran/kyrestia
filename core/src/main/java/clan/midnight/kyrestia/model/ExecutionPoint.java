package clan.midnight.kyrestia.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public interface ExecutionPoint {
    Execution execution();

    enum Status {RUNNABLE, WAITING, TERMINATED}
    Status status();

    Node currentNode();

    enum NodeStage {ENTERING, EXECUTING, LEAVING}
    NodeStage currentNodeStage();

    ExecutionPoint supExecutionPoint();

    Collection<ExecutionPoint> subExecutionPoints();

    Map<String, Serializable> localContext();

    String waitEvent();

    boolean isWaitingBereaved();
}
