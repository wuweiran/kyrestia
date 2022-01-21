package clan.midnight.kyrestia.model;

import java.util.Collection;

public interface Execution {
    String id();

    Process process();

    Collection<ExecutionPoint> executionPoints();

    enum Status {NEW, RUNNING, SUSPEND, TERMINATED}
    Status status();

    void run();

    void signal(String event);

    void pause();

    void proceed();
}
