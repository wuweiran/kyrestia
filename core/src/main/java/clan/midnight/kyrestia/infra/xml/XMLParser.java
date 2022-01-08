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
        StringBuilder sb = new StringBuilder();
        while (nextElement(reader, sb)) {
            Element childElement = parseElement(reader);
            currentElement.addChildElement(childElement);
        }
        if (sb.length() != 0) {
            currentElement.setTextContent(sb.toString());
        }
        return  currentElement;
    }

    private boolean nextElement(XMLStreamReader reader) throws XMLStreamException {
        while (reader.hasNext()) {
            int eventType = reader.next();
            if (eventType == XMLStreamConstants.END_ELEMENT) {
                return false;
            } else if (eventType == XMLStreamConstants.START_ELEMENT) {
                return true;
            }
        }

        return false;
    }

    private boolean nextElement(XMLStreamReader reader, StringBuilder sb) throws XMLStreamException {
        while (reader.hasNext()) {
            int eventType = reader.next();
            if (eventType == XMLStreamConstants.END_ELEMENT) {
                return false;
            } else if (eventType == XMLStreamConstants.START_ELEMENT) {
                return true;
            } else if (eventType == XMLStreamConstants.CHARACTERS
                    || eventType == XMLStreamConstants.CDATA) {
                String text = reader.getText();
                if (!isBlank(text)) {
                    sb.append(text);
                }
            }
        }

        return false;
    }

    private boolean isBlank(String text) {
        int textLen = text.length();
        for (int i = 0; i < textLen; i++) {
            if (!Character.isWhitespace(text.charAt(i))) {
                return false;
            }
        }

        return true;
    }
}
