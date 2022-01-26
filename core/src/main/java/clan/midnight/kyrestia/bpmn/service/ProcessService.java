package clan.midnight.kyrestia.bpmn.service;

import clan.midnight.kyrestia.bpmn.model.BpmnProcess;
import clan.midnight.kyrestia.bpmn.model.Definitions;
import clan.midnight.kyrestia.bpmn.model.support.ElementFactory;
import clan.midnight.kyrestia.infra.xml.Element;
import clan.midnight.kyrestia.infra.xml.XMLParseException;
import clan.midnight.kyrestia.infra.xml.XMLParser;

import java.io.InputStream;
import java.util.List;

public class ProcessService {
    private ProcessService() {}

    public static List<BpmnProcess> loadProcesses(InputStream inputStream) throws XMLParseException {
        Element processXmlElement = new XMLParser().parseFirstElement(inputStream);
        ElementFactory elementFactory = new ElementFactory();
        Definitions definitions = (Definitions) elementFactory.getIdBasedElement(processXmlElement);
        return definitions.getProcesses();
    }
}
