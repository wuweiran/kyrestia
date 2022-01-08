package clan.midnight.kyrestia.bpmn.model.event;

import clan.midnight.kyrestia.bpmn.model.IdBasedElement;
import clan.midnight.kyrestia.bpmn.model.flow.SequenceFlow;
import clan.midnight.kyrestia.bpmn.model.support.XmlReference;
import clan.midnight.kyrestia.infra.spi.TypeBinding;

import java.util.ArrayList;

@TypeBinding("bpmn:endEvent")
public class EndEvent extends IdBasedElement {
    @XmlReference(type = XmlReference.Type.CHILD_ELEMENT_REF, value = "bpmn:incoming")
    private final ArrayList<SequenceFlow> incomingSequenceFlowList = new ArrayList<>(4);
}
