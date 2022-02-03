package clan.midnight.kyrestia.bpmn.model.activity.task;

import clan.midnight.kyrestia.bpmn.Delegation;
import clan.midnight.kyrestia.bpmn.DelegationContext;
import clan.midnight.kyrestia.bpmn.ProcessDefinitionException;
import clan.midnight.kyrestia.bpmn.model.BpmnProcess;
import clan.midnight.kyrestia.bpmn.model.IdBasedElement;
import clan.midnight.kyrestia.bpmn.model.flow.SequenceFlow;
import clan.midnight.kyrestia.bpmn.model.support.ElementInit;
import clan.midnight.kyrestia.bpmn.model.support.XmlReference;
import clan.midnight.kyrestia.config.Configuration;
import clan.midnight.kyrestia.infra.spi.TypeBinding;
import clan.midnight.kyrestia.model.Node;
import clan.midnight.kyrestia.model.RuntimeExecutionPoint;

import java.util.ArrayList;

@TypeBinding("bpmn:sendTask")
public class SendTask extends IdBasedElement implements Node {
    @XmlReference(type = XmlReference.Type.ANCESTOR_ELEMENT, value = "bpmn:process")
    private BpmnProcess process;

    @XmlReference(type = XmlReference.Type.CHILD_ELEMENT_REF, value = "bpmn:incoming")
    private final ArrayList<SequenceFlow> incomingSequenceFlowList = new ArrayList<>(2);

    @XmlReference(type = XmlReference.Type.CHILD_ELEMENT_REF, value = "bpmn:outgoing")
    private final ArrayList<SequenceFlow> outGoingSequenceFlowList = new ArrayList<>(1);
    
    @XmlReference(type = XmlReference.Type.ATTRIBUTE, value = "camunda:class")
    private String camundaClass;
    
    @XmlReference(type = XmlReference.Type.ATTRIBUTE, value = "smart:class")
    private String smartClass;

    private Delegation delegation;

    @ElementInit
    public void checkOutgoingUniqueness() {
        if (outGoingSequenceFlowList.size() != 1) {
            throw new ProcessDefinitionException("[BPMN] Send task has more than one or zero " +
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

    @ElementInit
    public void checkAndInitializeDelegation() {
        if (camundaClass != null && smartClass != null) {
            throw new ProcessDefinitionException("[BPMN] Cannot be delegated by multiple delegations, id: " + getId());
        }
        if (camundaClass != null) {
            Object delegationObject = Configuration.implementationAccessor.access(camundaClass);
            if (!(delegationObject instanceof Delegation)) {
                throw new ProcessDefinitionException("[BPMN] Inaccessible or invalid delegation: " + camundaClass);
            }
            delegation = (Delegation) delegationObject;
        } else if (smartClass != null) {
            Object delegationObject = Configuration.implementationAccessor.access(smartClass);
            if (!(delegationObject instanceof Delegation)) {
                throw new ProcessDefinitionException("[BPMN] Inaccessible or invalid delegation: " + smartClass);
            }
            delegation = (Delegation) delegationObject;
        }
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
        if (delegation != null) {
            delegation.execute(new DelegationContext(executionPoint));
        }
    }

    @Override
    public Node leave(RuntimeExecutionPoint executionPoint) {
        return getOutgoingSequenceFlow().getTargetNode();
    }
}
