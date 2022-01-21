package clan.midnight.kyrestia.bpmn.model.activity.task;

import clan.midnight.kyrestia.bpmn.model.Message;
import clan.midnight.kyrestia.bpmn.model.support.ElementTestUtils;
import clan.midnight.kyrestia.infra.xml.Element;
import clan.midnight.kyrestia.model.Execution;
import clan.midnight.kyrestia.model.Process;
import clan.midnight.kyrestia.pvm.SingleThreadSyncExecution;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReceiveTaskTest {
    @Test
    void testWaitMessage() {
        Element receiveTaskXmlElement = ElementTestUtils.createBpmnXmlElementWithId("receiveTask", "test");
        Element processXmlElement = ElementTestUtils.createSimpleProcessXmlElementFromXmlElement(receiveTaskXmlElement);
        Element messageXmlElement = ElementTestUtils.createBpmnXmlElementWithId("message", "test_message_0");
        receiveTaskXmlElement.addAttributeValue("messageRef", messageXmlElement.getAttributeValue("id"));
        processXmlElement.addChildElement(messageXmlElement);
        Process process = ElementTestUtils.createProcessFromProcessXmlElement(processXmlElement);

        Execution execution = new SingleThreadSyncExecution(process);
        execution.run();

        assertEquals(Execution.Status.RUNNING, execution.status());

        execution.signal(messageXmlElement.getAttributeValue("id"));

        assertEquals(Execution.Status.TERMINATED, execution.status());
    }

    @Test
    void testRunDelegation() {
        Element receiveTaskXmlElement = ElementTestUtils.createBpmnXmlElementWithId("receiveTask", "test");
        Element processXmlElement = ElementTestUtils.createSimpleProcessXmlElementFromXmlElement(receiveTaskXmlElement);
        receiveTaskXmlElement.addAttributeValue("smart:class", TestDelegation.class.getName());
        Process process = ElementTestUtils.createProcessFromProcessXmlElement(processXmlElement);

        Execution execution = new SingleThreadSyncExecution(process);
        execution.run();
        execution.signal(Message.GLOBAL_DEFAULT_MESSAGE.getId());

        assertEquals(Execution.Status.TERMINATED, execution.status());
    }
}