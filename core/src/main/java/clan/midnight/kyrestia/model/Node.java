package clan.midnight.kyrestia.model;

public interface Node {
    void enter(RuntimeExecutionPoint executionPoint);

    void execute(RuntimeExecutionPoint executionPoint);

    Node leave(RuntimeExecutionPoint executionPoint);
}
