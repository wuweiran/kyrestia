package clan.midnight.kyrestia.bpmn.model.support;

import clan.midnight.kyrestia.bpmn.ProcessDefinitionException;
import clan.midnight.kyrestia.bpmn.model.BpmnProcess;
import clan.midnight.kyrestia.bpmn.model.Definitions;
import clan.midnight.kyrestia.bpmn.model.event.StartEvent;
import clan.midnight.kyrestia.bpmn.model.flow.SequenceFlow;
import clan.midnight.kyrestia.infra.xml.Element;
import clan.midnight.kyrestia.infra.xml.XMLParseException;
import clan.midnight.kyrestia.infra.xml.XMLParser;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class ElementFactoryTest {
    @Test
    void testGetElement() throws XMLParseException {
        InputStream inputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("bpmn-element-factory.xml");
        Element processXmlElement = new XMLParser().parseFirstElement(inputStream);

        ElementFactory elementFactory = new ElementFactory();
        Definitions definitions = (Definitions) elementFactory.getElement(processXmlElement);

        assertFalse(definitions.getProcesses().isEmpty());

        BpmnProcess process = definitions.getProcesses().get(0);

        assertNotNull(process);

        StartEvent startEvent = process.getStartEvent();

        assertNotNull(startEvent);

        SequenceFlow firstTransition = startEvent.getOutgoingSequenceFlow();

        assertSame(startEvent, firstTransition.getSourceElement());
        assertNotNull(firstTransition.getTargetElement());
    }

    @Test
    void testGetElementWithIncorrectDefinition() throws XMLParseException {

        InputStream inputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("bpmn-element-factory-incorrect.xml");
        Element processXmlElement = new XMLParser().parseFirstElement(inputStream);
        ElementFactory elementFactory = new ElementFactory();

        assertThrows(IllegalStateException.class, () -> elementFactory.getElement(processXmlElement));
    }
}