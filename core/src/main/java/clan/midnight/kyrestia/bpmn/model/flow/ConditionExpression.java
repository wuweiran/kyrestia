package clan.midnight.kyrestia.bpmn.model.flow;

import clan.midnight.kyrestia.bpmn.model.BpmnElement;
import clan.midnight.kyrestia.bpmn.model.support.XmlReference;
import clan.midnight.kyrestia.config.Configuration;
import clan.midnight.kyrestia.infra.spi.TypeBinding;

import java.util.Map;

@TypeBinding("bpmn:conditionExpression")
public class ConditionExpression implements BpmnElement {
    @XmlReference(type = XmlReference.Type.TEXT, value = "unnecessary")
    private String expression;

    public boolean match(Map<String, Object> context) {
        if (expression == null) {
            return true;
        }
        return Configuration.expressionEvaluator.evaluate(expression, context);
    }
}
