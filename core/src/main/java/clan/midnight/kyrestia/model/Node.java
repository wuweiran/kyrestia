package clan.midnight.kyrestia.model;

public interface Node {
    String id();

    void enter(RuntimeExecutionPoint executionPoint);

    void execute(RuntimeExecutionPoint executionPoint);

    Node leave(RuntimeExecutionPoint executionPoint);
}
