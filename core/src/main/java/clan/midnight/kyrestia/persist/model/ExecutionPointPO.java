package clan.midnight.kyrestia.persist.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ExecutionPointPO {
    String status;
    String currentNodeId;
    String currentNodeStage;
    List<ExecutionPointPO> subExecutionPoints;
    Map<String, Serializable> localContext;
    String waitEvent;
    Boolean isWaitingBereaved;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentNodeId() {
        return currentNodeId;
    }

    public void setCurrentNodeId(String currentNodeId) {
        this.currentNodeId = currentNodeId;
    }

    public String getCurrentNodeStage() {
        return currentNodeStage;
    }

    public void setCurrentNodeStage(String currentNodeStage) {
        this.currentNodeStage = currentNodeStage;
    }

    public List<ExecutionPointPO> getSubExecutionPoints() {
        return subExecutionPoints;
    }

    public void setSubExecutionPoints(List<ExecutionPointPO> subExecutionPoints) {
        this.subExecutionPoints = subExecutionPoints;
    }

    public Map<String, Serializable> getLocalContext() {
        return localContext;
    }

    public void setLocalContext(Map<String, Serializable> localContext) {
        this.localContext = localContext;
    }

    public String getWaitEvent() {
        return waitEvent;
    }

    public void setWaitEvent(String waitEvent) {
        this.waitEvent = waitEvent;
    }

    public Boolean getWaitingBereaved() {
        return isWaitingBereaved;
    }

    public void setWaitingBereaved(Boolean waitingBereaved) {
        isWaitingBereaved = waitingBereaved;
    }

    @Override
    public String toString() {
        return "ExecutionPointPO{" +
                "status='" + status + '\'' +
                ", currentNodeId='" + currentNodeId + '\'' +
                ", currentNodeStage='" + currentNodeStage + '\'' +
                ", subExecutionPoints=" + subExecutionPoints +
                ", localContext=" + localContext +
                ", waitEvent='" + waitEvent + '\'' +
                ", isWaitingBereaved=" + isWaitingBereaved +
                '}';
    }
}
