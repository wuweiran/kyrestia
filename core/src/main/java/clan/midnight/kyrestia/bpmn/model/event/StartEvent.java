package clan.midnight.kyrestia.bpmn.model.event;

import clan.midnight.kyrestia.bpmn.ProcessDefinitionException;
import clan.midnight.kyrestia.bpmn.model.BpmnProcess;
import clan.midnight.kyrestia.bpmn.model.IdBasedElement;
import clan.midnight.kyrestia.bpmn.model.flow.SequenceFlow;
import clan.midnight.kyrestia.bpmn.model.support.ElementInit;
import clan.midnight.kyrestia.bpmn.model.support.XmlReference;
import clan.midnight.kyrestia.infra.spi.TypeBinding;
import clan.midnight.kyrestia.model.Node;
import clan.midnight.kyrestia.model.RuntimeExecutionPoint;

import java.util.ArrayList;

@TypeBinding("bpmn:startEvent")
public class StartEvent extends IdBasedElement implements Node {
    @XmlReference(type = XmlReference.Type.ANCESTOR_ELEMENT, value = "bpmn:process")
    private BpmnProcess process;

    @XmlReference(type = XmlReference.Type.CHILD_ELEMENT_REF, value = "bpmn:outgoing")
    private final ArrayList<SequenceFlow> outGoingSequenceFlowList = new ArrayList<>(1);

    @ElementInit
    public void checkOutgoingUniqueness() {
        if (outGoingSequenceFlowList.size() != 1) {
            throw new ProcessDefinitionException("[BPMN] Start event has more than one or zero " +
                    "outgoing sequence flow, id: " + getId());
        }
    }

    @ElementInit
    public void registerToProcess() {
        process.registerNode(this);
    }

    public SequenceFlow getOutgoingSequenceFlow() {
        return outGoingSequenceFlowList.get(0);
    }

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
        return getOutgoingSequenceFlow().getTargetNode();
    }
}
