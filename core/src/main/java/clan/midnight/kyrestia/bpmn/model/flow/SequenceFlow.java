package clan.midnight.kyrestia.bpmn.model.flow;

import clan.midnight.kyrestia.bpmn.model.IdBasedElement;
import clan.midnight.kyrestia.bpmn.model.support.XmlReference;
import clan.midnight.kyrestia.infra.spi.TypeBinding;
import clan.midnight.kyrestia.model.Node;
import clan.midnight.kyrestia.model.RuntimeExecutionPoint;

@TypeBinding("bpmn:sequenceFlow")
public class SequenceFlow extends IdBasedElement implements Node {
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
        return (Node) targetElement;
    }
}
