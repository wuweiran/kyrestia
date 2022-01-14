package clan.midnight.kyrestia.bpmn.model.support;

import clan.midnight.kyrestia.infra.xml.Element;
import clan.midnight.kyrestia.model.Process;

import javax.xml.namespace.QName;

public class ElementTestUtils {
    private static final String BPMN_NAMESPACE_URI = "http://www.omg.org/spec/BPMN/20100524/MODEL";
    private static final String BPMN_PREFIX = "bpmn";

    private ElementTestUtils() {}

    public static Process createProcessFromProcessXmlElement(Element processXmlElement) {
        return (Process) new ElementFactory().getElement(processXmlElement);
    }

    public static Element createSimpleProcessXmlElementFromXmlElement(Element xmlElement) {
        Element processXmlElement = createBpmnXmlElementWithId("process", "process_0");
        Element startEventXmlElement = createBpmnXmlElementWithId("startEvent", "startEvent_0");
        processXmlElement.addChildElement(startEventXmlElement);
        startEventXmlElement.addChildElement(createBpmnRefXmlElement("outgoing", "startEvent_outgoing"));
        Element startEventOutGoingSequenceFlowXmlElement
                = createBpmnXmlElementWithId("sequenceFlow", "startEvent_outgoing");
        processXmlElement.addChildElement(startEventOutGoingSequenceFlowXmlElement);
        startEventOutGoingSequenceFlowXmlElement.addAttributeValue("sourceRef", "startEvent_0");
        startEventOutGoingSequenceFlowXmlElement.addAttributeValue("targetRef", xmlElement.getAttributeValue("id"));
        Element endEventIncomingSequenceFlowXmlElement
                = createBpmnXmlElementWithId("sequenceFlow", "endEvent_incoming");
        processXmlElement.addChildElement(endEventIncomingSequenceFlowXmlElement);
        processXmlElement.addChildElement(xmlElement);
        xmlElement.addChildElement(createBpmnRefXmlElement("incoming", "startEvent_outgoing"));
        xmlElement.addChildElement(createBpmnRefXmlElement("outgoing", "endEvent_incoming"));
        Element endEventXmlElement = createBpmnXmlElementWithId("endEvent", "endEvent_0");
        endEventXmlElement.addChildElement(createBpmnRefXmlElement("incoming", "endEvent_incoming"));
        return processXmlElement;
    }

    public static Element createBpmnXmlElementWithId(String localPart, String id) {
        Element element = new Element(new QName(BPMN_NAMESPACE_URI, localPart, BPMN_PREFIX));
        element.addAttributeValue("id", id);
        return element;
    }

    public static Element createBpmnRefXmlElement(String localPart, String refId) {
        Element element = new Element(new QName(BPMN_NAMESPACE_URI, localPart, BPMN_PREFIX));
        element.setTextContent(refId);
        return element;
    }
}
