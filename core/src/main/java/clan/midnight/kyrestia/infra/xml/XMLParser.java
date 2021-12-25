package clan.midnight.kyrestia.infra.xml;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;

public class XMLParser {
    public Element parseFirstElement(InputStream inputStream) throws XMLParseException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);

        XMLStreamReader reader = null;
        try {
            reader = factory.createXMLStreamReader(inputStream);

            boolean findFirst = nextElement(reader);
            if (findFirst) {
                return parseElement(reader);
            }

            throw new XMLParseException("[XML] Cannot find any elements when parsing");
        } catch (XMLStreamException xse) {
            throw new XMLParseException("[XML] Encountered error when reading XML stream: " + xse.getMessage(), xse);
        } catch (Exception e) {
            throw new XMLParseException("[XML] Unknown exception: " + e.getMessage(), e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (XMLStreamException e) {
                    // do nothing
                }
            }
        }
    }

    private Element parseElement(XMLStreamReader reader) throws XMLStreamException {
        Element currentElement = new Element(reader);
        while (nextElement(reader)) {
            Element childElement = parseElement(reader);
            currentElement.addChildElement(childElement);
        }
        return  currentElement;
    }

    private boolean nextElement(XMLStreamReader reader) throws XMLStreamException {
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.END_ELEMENT) {
                return false;
            } else if (event == XMLStreamConstants.START_ELEMENT) {
                return true;
            }
        }

        return false;
    }
}
