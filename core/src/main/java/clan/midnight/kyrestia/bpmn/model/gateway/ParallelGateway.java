package clan.midnight.kyrestia.bpmn.model.gateway;

import clan.midnight.kyrestia.bpmn.model.IdBasedElement;
import clan.midnight.kyrestia.bpmn.model.flow.SequenceFlow;
import clan.midnight.kyrestia.bpmn.model.support.XmlReference;
import clan.midnight.kyrestia.infra.spi.TypeBinding;
import clan.midnight.kyrestia.model.Node;
import clan.midnight.kyrestia.model.RuntimeExecutionPoint;

import java.util.ArrayList;

@TypeBinding("bpmn:parallelGateway")
public class ParallelGateway extends IdBasedElement implements Node {
    @XmlReference(type = XmlReference.Type.CHILD_ELEMENT_REF, value = "bpmn:incoming")
    private final ArrayList<SequenceFlow> incomingSequenceFlowList = new ArrayList<>(2);

    @XmlReference(type = XmlReference.Type.CHILD_ELEMENT_REF, value = "bpmn:outgoing")
    private final ArrayList<SequenceFlow> outGoingSequenceFlowList = new ArrayList<>(1);

    @Override
    public void enter(RuntimeExecutionPoint executionPoint) {
        // do nothing
    }

    @Override
    public void execute(RuntimeExecutionPoint executionPoint) {
        for (SequenceFlow sequenceFlow : outGoingSequenceFlowList) {
            if (sequenceFlow.match(executionPoint)) {
                executionPoint.newExecutionPointOn(sequenceFlow.getTargetNode());
            }
        }
    }

    @Override
    public Node leave(RuntimeExecutionPoint executionPoint) {
        return null;
    }
}
