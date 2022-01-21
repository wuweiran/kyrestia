package clan.midnight.kyrestia.bpmn.model.flow;

import clan.midnight.kyrestia.bpmn.model.IdBasedElement;
import clan.midnight.kyrestia.bpmn.model.support.XmlReference;
import clan.midnight.kyrestia.infra.spi.TypeBinding;
import clan.midnight.kyrestia.model.Node;
import clan.midnight.kyrestia.model.RuntimeExecutionPoint;

import java.util.ArrayList;
import java.util.Map;

@TypeBinding("bpmn:sequenceFlow")
public class SequenceFlow extends IdBasedElement {
    @XmlReference(type = XmlReference.Type.ATTRIBUTE_REF, value = "sourceRef")
    private IdBasedElement sourceElement;

    @XmlReference(type = XmlReference.Type.ATTRIBUTE_REF, value = "targetRef")
    private IdBasedElement targetElement;

    @XmlReference(type = XmlReference.Type.CHILD_ELEMENT, value = "bpmn:conditionExpression")
    private final ArrayList<ConditionExpression> conditionExpressionList = new ArrayList<>(1);

    public IdBasedElement getSourceElement() {
        return sourceElement;
    }

    public IdBasedElement getTargetElement() {
        return targetElement;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public boolean match(RuntimeExecutionPoint executionPoint) {
        if (conditionExpressionList.isEmpty()) {
            return true;
        }

        Map<String, Object> contextMap = (Map) executionPoint.buildContextMap();
        for (ConditionExpression conditionExpression : conditionExpressionList) {
            if (!conditionExpression.match(contextMap)) {
                return false;
            }
        }
        return true;
    }

    public Node getTargetNode() {
        return (Node) targetElement;
    }
}
