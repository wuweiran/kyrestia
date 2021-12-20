package clan.midnight.kyrestia.model;

import java.util.Collection;

public interface Execution {
    Process getProcess();

    Collection<ExecutionPoint> getExecutionPoints();

    enum Status {NEW, RUNNING, SUSPEND, TERMINATED}
    Status getStatus();

    void run();

    void signal(String event);

    void pause();

    void resume();
}
