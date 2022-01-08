package clan.midnight.kyrestia.bpmn.model.flow;

import clan.midnight.kyrestia.bpmn.model.IdBasedElement;
import clan.midnight.kyrestia.bpmn.model.support.XmlReference;
import clan.midnight.kyrestia.infra.spi.TypeBinding;

@TypeBinding("bpmn:sequenceFlow")
public class SequenceFlow extends IdBasedElement {
    @XmlReference(type = XmlReference.Type.ATTRIBUTE_REF, value = "sourceRef")
    private IdBasedElement sourceElement;

    @XmlReference(type = XmlReference.Type.ATTRIBUTE_REF, value = "targetRef")
    private IdBasedElement targetElement;

    public IdBasedElement getSourceElement() {
        return sourceElement;
    }

    public IdBasedElement getTargetElement() {
        return targetElement;
    }
}
