package clan.midnight.kyrestia.model;

public interface Execution {
    String id();

    Process process();

    ExecutionPoint snapshot();

    enum Status {NEW, RUNNING, SUSPEND, TERMINATED}
    Status status();

    void run();

    void signal(String event);

    void pause();

    void proceed();
}
