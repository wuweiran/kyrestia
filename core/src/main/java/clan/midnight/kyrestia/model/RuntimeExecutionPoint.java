package clan.midnight.kyrestia.model;

import java.io.Serializable;
import java.util.Map;

public interface RuntimeExecutionPoint {
    void putContext(String key, Serializable value);

    <T extends Serializable> T getContext(String key);

    Map<String, Serializable> getContextMap();

    RuntimeExecutionPoint newExecutionPointOn(Node node);

    void await(String event);

    void awaitBereaved();
}
