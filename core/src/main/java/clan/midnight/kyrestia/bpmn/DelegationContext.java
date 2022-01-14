package clan.midnight.kyrestia.bpmn;

import clan.midnight.kyrestia.model.RuntimeExecutionPoint;

import java.io.Serializable;

public class DelegationContext {
    private final RuntimeExecutionPoint executionPoint;

    public DelegationContext(RuntimeExecutionPoint executionPoint) {
        this.executionPoint = executionPoint;
    }

    public <T extends Serializable> T get(String key) {
        return executionPoint.getContext(key);
    }

    public void put(String key, Serializable value) {
        executionPoint.putContext(key, value);
    }
}
