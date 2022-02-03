package clan.midnight.kyrestia.model;

public interface Process {
    String id();

    Node startNode();

    Node node(String nodeId);
}
