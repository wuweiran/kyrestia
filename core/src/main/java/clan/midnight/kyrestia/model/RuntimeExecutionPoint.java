package clan.midnight.kyrestia.model;

import java.io.Serializable;

public interface RuntimeExecutionPoint {
    void putContext(String key, Serializable value);

    <T extends Serializable> T getContext(String key);

    RuntimeExecutionPoint newExecutionPointOn(Node node);

    void await(String event);

    void awaitBereaved();
}
