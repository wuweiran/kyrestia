package clan.midnight.kyrestia.infra.xml;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class XMLParserTest {
    @Test
    void testParseProcess() throws XMLParseException {
        InputStream inputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("xml-parser-process.xml");
        Element processElement = new XMLParser().parseFirstElement(inputStream);

        assertNotNull(processElement);
        assertEquals("Definitions_12vhdwp", processElement.getAttributeValue("id"));
        assertTrue(processElement.getChildElement().stream()
                .anyMatch(element -> "process".equals(element.getType().getLocalPart())));
    }

    @Test
    void testParseTextContent() throws XMLParseException {
        InputStream inputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("xml-parser-text-content.xml");
        Element processElement = new XMLParser().parseFirstElement(inputStream);

        assertTrue(processElement.getChildElement().stream()
                .filter(element -> "process".equals(element.getType().getLocalPart()))
                .map(Element::getChildElement)
                .flatMap(Collection::stream)
                .filter(element -> "receiveTask".equals(element.getType().getLocalPart()))
                .map(Element::getChildElement)
                .flatMap(Collection::stream)
                .allMatch(element -> element.getTextContent() != null));
    }
}