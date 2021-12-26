package clan.midnight.kyrestia.infra.xml;

import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class XMLParserTest {
    @Test
    void testParse() throws XMLParseException {
        InputStream inputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("xml-parser-test.xml");
        Element processElement = new XMLParser().parseFirstElement(inputStream);

        assertNotNull(processElement);
        assertEquals("Definitions_12vhdwp", processElement.getAttributeValue("id"));
        assertTrue(processElement.getChildElement().stream()
                .anyMatch(element -> element.getType().getLocalPart().equals("process")));
    }
}