package clan.midnight.kyrestia.bpmn.model.event;

import clan.midnight.kyrestia.bpmn.model.BpmnProcess;
import clan.midnight.kyrestia.bpmn.model.IdBasedElement;
import clan.midnight.kyrestia.bpmn.model.flow.SequenceFlow;
import clan.midnight.kyrestia.bpmn.model.support.XmlReference;
import clan.midnight.kyrestia.infra.spi.TypeBinding;
import clan.midnight.kyrestia.model.Node;
import clan.midnight.kyrestia.model.RuntimeExecutionPoint;

import java.util.ArrayList;

@TypeBinding("bpmn:endEvent")
public class EndEvent extends IdBasedElement implements Node {
    @XmlReference(type = XmlReference.Type.ANCESTOR_ELEMENT, value = "bpmn:process")
    private BpmnProcess process;

    @XmlReference(type = XmlReference.Type.CHILD_ELEMENT_REF, value = "bpmn:incoming")
    private final ArrayList<SequenceFlow> incomingSequenceFlowList = new ArrayList<>(4);

    @Override
    public String id() {
        return process.getId() + ":" + getId();
    }

    @Override
    public void enter(RuntimeExecutionPoint executionPoint) {
        // do nothing
    }

    @Override
    public void execute(RuntimeExecutionPoint executionPoint) {
        // do nothing
    }

    @Override
    public Node leave(RuntimeExecutionPoint executionPoint) {
        return null;
    }
}
