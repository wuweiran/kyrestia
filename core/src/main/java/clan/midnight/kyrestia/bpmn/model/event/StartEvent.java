package clan.midnight.kyrestia.bpmn.model.event;

import clan.midnight.kyrestia.bpmn.ProcessDefinitionException;
import clan.midnight.kyrestia.bpmn.model.IdBasedElement;
import clan.midnight.kyrestia.bpmn.model.flow.SequenceFlow;
import clan.midnight.kyrestia.bpmn.model.support.ElementInit;
import clan.midnight.kyrestia.bpmn.model.support.XmlReference;
import clan.midnight.kyrestia.infra.spi.TypeBinding;

import java.util.ArrayList;

@TypeBinding("bpmn:startEvent")
public class StartEvent extends IdBasedElement {
    @XmlReference(type = XmlReference.Type.CHILD_ELEMENT_REF, value = "bpmn:outgoing")
    private final ArrayList<SequenceFlow> outGoingSequenceFlowList = new ArrayList<>(1);

    @ElementInit
    public void check() {
        if (outGoingSequenceFlowList.size() != 1) {
            throw new ProcessDefinitionException("[BPMN] Start event has more than one or zero " +
                    "outgoing sequence flow, id: " + getId());
        }
    }

    public SequenceFlow getOutgoingSequenceFlow() {
        return outGoingSequenceFlowList.get(0);
    }
}
