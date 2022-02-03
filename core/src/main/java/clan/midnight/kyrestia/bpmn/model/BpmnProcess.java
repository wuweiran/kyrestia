package clan.midnight.kyrestia.bpmn.model;

import clan.midnight.kyrestia.bpmn.ProcessDefinitionException;
import clan.midnight.kyrestia.bpmn.model.event.StartEvent;
import clan.midnight.kyrestia.bpmn.model.support.ElementInit;
import clan.midnight.kyrestia.bpmn.model.support.XmlReference;
import clan.midnight.kyrestia.infra.spi.TypeBinding;
import clan.midnight.kyrestia.model.Node;
import clan.midnight.kyrestia.model.Process;

import java.util.ArrayList;
import java.util.HashMap;

@TypeBinding("bpmn:process")
public class BpmnProcess extends IdBasedElement implements Process {
    @XmlReference(type = XmlReference.Type.CHILD_ELEMENT, value = "bpmn:startEvent")
    private final ArrayList<StartEvent> startEventList = new ArrayList<>(1);

    private final HashMap<String, Node> nodeMap = new HashMap<>(16);

    @ElementInit
    public void checkStartEventUniqueness() {
        if (startEventList.size() != 1) {
            throw new ProcessDefinitionException("[BPMN] Process has more than one or zero start event, id: " + getId());
        }
    }

    public StartEvent getStartEvent() {
        return startEventList.get(0);
    }

    public void registerNode(Node node) {
        nodeMap.put(node.id(), node);
    }

    @Override
    public String id() {
        return getId();
    }

    @Override
    public Node startNode() {
        return getStartEvent();
    }

    @Override
    public Node node(String nodeId) {
        return nodeMap.get(nodeId);
    }
}
