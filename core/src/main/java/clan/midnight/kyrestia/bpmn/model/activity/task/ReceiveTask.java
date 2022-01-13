package clan.midnight.kyrestia.bpmn.model.activity.task;

import clan.midnight.kyrestia.bpmn.ProcessDefinitionException;
import clan.midnight.kyrestia.bpmn.model.IdBasedElement;
import clan.midnight.kyrestia.bpmn.model.Message;
import clan.midnight.kyrestia.bpmn.model.flow.SequenceFlow;
import clan.midnight.kyrestia.bpmn.model.support.ElementInit;
import clan.midnight.kyrestia.bpmn.model.support.XmlReference;
import clan.midnight.kyrestia.infra.spi.TypeBinding;
import clan.midnight.kyrestia.model.Node;
import clan.midnight.kyrestia.model.RuntimeExecutionPoint;

import java.util.ArrayList;

@TypeBinding("bpmn:receiveTask")
public class ReceiveTask extends IdBasedElement implements Node {
    @XmlReference(type = XmlReference.Type.CHILD_ELEMENT_REF, value = "bpmn:incoming")
    private final ArrayList<SequenceFlow> incomingSequenceFlowList = new ArrayList<>(2);

    @XmlReference(type = XmlReference.Type.CHILD_ELEMENT_REF, value = "bpmn:outgoing")
    private final ArrayList<SequenceFlow> outGoingSequenceFlowList = new ArrayList<>(1);

    @XmlReference(type = XmlReference.Type.ATTRIBUTE_REF, value = "messageRef")
    private Message message;

    @ElementInit
    public void check() {
        if (outGoingSequenceFlowList.size() != 1) {
            throw new ProcessDefinitionException("[BPMN] Receive task has more than one or zero " +
                    "outgoing sequence flow, id: " + getId());
        }
    }

    public SequenceFlow getOutgoingSequenceFlow() {
        return outGoingSequenceFlowList.get(0);
    }

    @Override
    public void enter(RuntimeExecutionPoint executionPoint) {
        Message waitingMessage = message == null ? Message.GLOBAL_DEFAULT_MESSAGE : message;
        executionPoint.await(waitingMessage.getId());
    }

    @Override
    public void execute(RuntimeExecutionPoint executionPoint) {
        // do nothing
    }

    @Override
    public Node leave(RuntimeExecutionPoint executionPoint) {
        return getOutgoingSequenceFlow();
    }
}
