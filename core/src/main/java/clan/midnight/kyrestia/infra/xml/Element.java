package clan.midnight.kyrestia.infra.xml;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Element {
    private final QName type;
    private final ArrayList<Attribute> attributeList;
    private ArrayList<Element> childElementList;

    Element(XMLStreamReader reader) {
        if (reader.getEventType() != XMLStreamConstants.START_ELEMENT) {
            throw new IllegalArgumentException(
                    "[XML] Element initialized at a wrong position, XML reader event type:" + reader.getEventType());
        }
        this.type = reader.getName();
        int attributeCount = reader.getAttributeCount();
        this.attributeList = new ArrayList<>(attributeCount);
        for (int i = 0; i < attributeCount; i++) {
            attributeList.add(new Attribute(reader.getAttributeName(i), reader.getAttributeValue(i)));
        }
        this.childElementList = null;
    }

    public QName getType() {
        return type;
    }

    public String getAttributeValue(QName key) {
        for (Attribute attribute : attributeList) {
            if (attribute.getKey() != null && attribute.getKey().equals(key)) {
                return attribute.getValue();
            }
        }

        return null;
    }

    public String getAttributeValue(String key) {
        for (Attribute attribute : attributeList) {
            QName attributeKey = attribute.getKey();
            if (attributeKey != null && XMLConstants.NULL_NS_URI.equals(attributeKey.getNamespaceURI())
                    && attributeKey.getLocalPart() != null && attributeKey.getLocalPart().equals(key)) {
                return attribute.getValue();
            }
        }

        return null;
    }

    public void addChildElement(Element element) {
        if (childElementList == null) {
            childElementList = new ArrayList<>();
        }

        childElementList.add(element);
    }

    public List<Element> getChildElement() {
        return childElementList == null ? Collections.emptyList() : Collections.unmodifiableList(childElementList);
    }
}
