package clan.midnight.kyrestia.bpmn.model.activity;

import clan.midnight.kyrestia.bpmn.ProcessDefinitionException;
import clan.midnight.kyrestia.bpmn.model.BpmnProcess;
import clan.midnight.kyrestia.bpmn.model.IdBasedElement;
import clan.midnight.kyrestia.bpmn.model.event.StartEvent;
import clan.midnight.kyrestia.bpmn.model.support.ElementInit;
import clan.midnight.kyrestia.bpmn.model.support.XmlReference;
import clan.midnight.kyrestia.infra.spi.TypeBinding;
import clan.midnight.kyrestia.model.Node;
import clan.midnight.kyrestia.model.RuntimeExecutionPoint;

import java.util.ArrayList;

@TypeBinding("bpmn:subProcess")
public class SubProcess extends IdBasedElement implements Node {
    @XmlReference(type = XmlReference.Type.ANCESTOR_ELEMENT, value = "bpmn:process")
    private BpmnProcess process;

    @XmlReference(type = XmlReference.Type.CHILD_ELEMENT, value = "bpmn:startEvent")
    private final ArrayList<StartEvent> startEventList = new ArrayList<>(1);

    @ElementInit
    public void checkStartEventUniqueness() {
        if (startEventList.size() != 1) {
            throw new ProcessDefinitionException("[BPMN] Process has more than one or zero start event, id: " + getId());
        }
    }

    public StartEvent getStartEvent() {
        return startEventList.get(0);
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
        return getStartEvent();
    }
}
