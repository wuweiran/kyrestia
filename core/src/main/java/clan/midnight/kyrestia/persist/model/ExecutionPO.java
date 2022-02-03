package clan.midnight.kyrestia.persist.model;

public class ExecutionPO {
    String id;
    String processId;
    ExecutionPointPO mainExecutionPoint;
    String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public ExecutionPointPO getMainExecutionPoint() {
        return mainExecutionPoint;
    }

    public void setMainExecutionPoint(ExecutionPointPO mainExecutionPoint) {
        this.mainExecutionPoint = mainExecutionPoint;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ExecutionPO{" +
                "id='" + id + '\'' +
                ", processId='" + processId + '\'' +
                ", mainExecutionPoint=" + mainExecutionPoint +
                ", status='" + status + '\'' +
                '}';
    }
}
