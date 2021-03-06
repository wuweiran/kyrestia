package clan.midnight.kyrestia.bpmn.model.support;

import clan.midnight.kyrestia.bpmn.model.BpmnElement;
import clan.midnight.kyrestia.bpmn.model.Definitions;
import clan.midnight.kyrestia.bpmn.model.IdBasedElement;
import clan.midnight.kyrestia.infra.reflect.ClassUtils;
import clan.midnight.kyrestia.infra.spi.TypeBindingScanner;
import clan.midnight.kyrestia.infra.xml.Element;
import clan.midnight.kyrestia.infra.xml.XMLUtils;

import javax.xml.namespace.QName;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ElementFactory extends ElementRegistry {
    private static final String ID_KEY = "id";
    private static final TypeBindingScanner<IdBasedElement> SCANNER =
            new TypeBindingScanner<>(IdBasedElement.class, Definitions.class.getPackage().getName());

    static {
        SCANNER.scan();
    }

    private final Map<String, Element> contextXmlElementCache;

    public ElementFactory() {
        this.contextXmlElementCache = new HashMap<>(64);
    }

    public IdBasedElement getIdBasedElement(Element xmlElement) {
        String id = xmlElement.getAttributeValue(ID_KEY);
        if (id == null) {
            throw new IllegalArgumentException("[BPMN] Converting an XML element without id: " + xmlElement);
        }
        Class<? extends IdBasedElement> type = getTypeByXmlElementType(xmlElement.getType());
        if (type == null) {
            throw new IllegalArgumentException("[BPMN] Converting an XML element with unsupported type: " + xmlElement);
        }

        putContextXmlElement(xmlElement);
        return doGetIdBasedElement(id, type, xmlElement);
    }

    private Class<? extends IdBasedElement> getTypeByXmlElementType(QName type) {
        return SCANNER.getType(XMLUtils.getQNameDisplay(type));
    }

    private void putContextXmlElement(Element xmlElement) {
        String id = xmlElement.getAttributeValue(ID_KEY);
        if (id == null || contextXmlElementCache.containsKey(id)) {
            return;
        }
        contextXmlElementCache.put(id, xmlElement);
        for (Element childXmlElement : xmlElement.getChildElement()) {
            putContextXmlElement(childXmlElement);
        }
    }

    private Element getContextXmlElement(String id) {
        Element xmlElement = contextXmlElementCache.get(id);
        if (xmlElement == null) {
            throw new IllegalStateException("[BPMN] Element is not in context with id :" + id);
        }
        return xmlElement;
    }

    private IdBasedElement doGetIdBasedElement(String id, Class<? extends IdBasedElement> type, Element currentXmlElement) {
        IdBasedElement element = getIdBasedElement(id);
        if (element != null) {
            return element;
        }
        if (isIdBasedElementCurrentlyInCreation(id)) {
            throw new IllegalStateException("[BPMN] Element currently in creation, id: " + id);
        }

        element = getIdBasedElement(id, type);
        beforeIdBasedElementCreation(id);
        try {
            populateElement(element, currentXmlElement);
            initializeElement(element);
        } catch (Throwable ex) {
            throw new IllegalStateException("[BPMN] Element creation failed, id: " + id, ex);
        }
        registerIdBasedElement(element);
        afterIdBaseElementCreation(id);
        return element;
    }

    private void populateElement(BpmnElement element, Element currentXmlElement) throws IllegalAccessException {
        Field[] fields = element.getClass().getDeclaredFields();
        for (Field field : fields) {
            XmlReference xmlReference = field.getAnnotation(XmlReference.class);
            if (xmlReference != null && xmlReference.value() != null) {
                field.setAccessible(true);
                String value = xmlReference.value();
                switch (xmlReference.type()) {
                    case ATTRIBUTE_REF:
                        resolveAttributeReference(value, field, element, currentXmlElement);
                        break;
                    case CHILD_ELEMENT_REF:
                        resolveChildElementReference(value, field, element, currentXmlElement);
                        break;
                    case CHILD_ELEMENT:
                        resolveChildElements(value, field, element, currentXmlElement);
                        break;
                    case ATTRIBUTE:
                        resolveAttribute(value, field, element, currentXmlElement);
                        break;
                    case TEXT:
                        resolveText(field, element, currentXmlElement);
                        break;
                    case ANCESTOR_ELEMENT:
                        resolveAncestorElement(value, field, element, currentXmlElement);
                        break;
                    default: // do nothing
                }
            }
        }
    }

    private void resolveAttributeReference(String xmlAttributeKey, Field field, BpmnElement element,
                                           Element xmlElement) throws IllegalAccessException {
        if (!IdBasedElement.class.isAssignableFrom(field.getType())) {
            throw new IllegalStateException("[BPMN] XmlReference with ATTRIBUTE_REF " +
                    "should be annotated on non-final fields of type that extends IdBasedElement");
        }
        String refId = xmlElement.getAttributeValue(xmlAttributeKey);
        if (refId != null) {
            Element refXmlElement = getContextXmlElement(refId);
            IdBasedElement refElement = getIdBasedElement(refXmlElement);
            field.set(element, refElement);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void resolveChildElementReference(String xmlElementTypeDisplay, Field field, BpmnElement element,
                                              Element xmlElement) throws IllegalAccessException {
        if (!Collection.class.isAssignableFrom(field.getType())) {
            throw new IllegalStateException("[BPMN] XmlReference with CHILD_ELEMENT_REF " +
                    "should be annotated on fields of type that extends Collection");
        }
        Collection refCollection = (Collection) field.get(element);
        for (Element childXmlElement : xmlElement.getChildElement()) {
            if (xmlElementTypeDisplay.equals(XMLUtils.getQNameDisplay(childXmlElement.getType()))) {
                String refId = childXmlElement.getTextContent();
                if (refId != null) {
                    Element refXmlElement = getContextXmlElement(refId);
                    IdBasedElement refElement = getIdBasedElement(refXmlElement);
                    refCollection.add(refElement);
                }
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void resolveChildElements(String xmlElementTypeDisplay, Field field, BpmnElement element,
                                      Element xmlElement) throws IllegalAccessException {
        if (!Collection.class.isAssignableFrom(field.getType())) {
            throw new IllegalStateException("[BPMN] XmlReference with CHILD_ELEMENT_REF " +
                    "should be annotated on fields of type that extends Collection");
        }
        Collection refCollection = (Collection) field.get(element);
        for (Element childXmlElement : xmlElement.getChildElement()) {
            if (xmlElementTypeDisplay.equals(XMLUtils.getQNameDisplay(childXmlElement.getType()))) {
                String refId = childXmlElement.getAttributeValue(ID_KEY);
                if (refId != null) {
                    Element refXmlElement = getContextXmlElement(refId);
                    IdBasedElement refElement = getIdBasedElement(refXmlElement);
                    refCollection.add(refElement);
                } else {
                    BpmnElement nonIdBasedElement = getNonIdBasedElement(childXmlElement);
                    refCollection.add(nonIdBasedElement);
                }
            }
        }
    }

    private void resolveAttribute(String xmlAttributeKey, Field field, BpmnElement element, Element xmlElement)
            throws IllegalAccessException {
        if (!String.class.equals(field.getType())) {
            throw new IllegalStateException("[BPMN] XmlReference with ATTRIBUTE " +
                    "should be annotated on a non-final String field");
        }
        String xmlAttributeValue = xmlElement.getAttributeValue(xmlAttributeKey);
        if (xmlAttributeValue != null) {
            field.set(element, xmlAttributeValue);
        }
    }

    private void resolveText(Field field, BpmnElement element, Element xmlElement)
            throws IllegalAccessException {
        if (!String.class.equals(field.getType())) {
            throw new IllegalStateException("[BPMN] XmlReference with TEXT " +
                    "should be annotated on a non-final String field");
        }
        String textContent = xmlElement.getTextContent();
        if (textContent != null) {
            field.set(element, textContent);
        }
    }

    private void resolveAncestorElement(String xmlElementTypeDisplay, Field field, BpmnElement element,
                                        Element xmlElement) throws IllegalAccessException {
        if (!BpmnElement.class.isAssignableFrom(field.getType())) {
            throw new IllegalStateException("[BPMN] XmlReference with PARENT_ELEMENT " +
                    "should be annotated on non-final fields of type that extends BpmnElement");
        }

        Element ancestorXmlElement = xmlElement.getParentElement();
        while (ancestorXmlElement != null) {
            if (xmlElementTypeDisplay.equals(XMLUtils.getQNameDisplay(ancestorXmlElement.getType()))) {
                String refId = ancestorXmlElement.getAttributeValue(ID_KEY);
                if (refId != null) {
                    IdBasedElement refElement = getIdBasedElement(ancestorXmlElement);
                    field.set(element, refElement);
                }
            }
            ancestorXmlElement = ancestorXmlElement.getParentElement();
        }
    }

    private void initializeElement(BpmnElement element) throws Throwable {
        Method[] methods = element.getClass().getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(ElementInit.class)) {
                try {
                    method.invoke(element);
                } catch (InvocationTargetException e) {
                    throw e.getTargetException();
                }
            }
        }
    }

    public BpmnElement getNonIdBasedElement(Element xmlElement) {
        String id = xmlElement.getAttributeValue(ID_KEY);
        if (id != null) {
            throw new IllegalArgumentException("[BPMN] Converting an XML element without id: " + xmlElement);
        }
        Class<? extends IdBasedElement> type = getTypeByXmlElementType(xmlElement.getType());
        if (type == null) {
            throw new IllegalArgumentException("[BPMN] Converting an XML element with unsupported type: " + xmlElement);
        }

        putContextXmlElement(xmlElement);
        return doGetNonIdBasedElement(type, xmlElement);
    }

    private BpmnElement doGetNonIdBasedElement(Class<? extends IdBasedElement> type, Element currentXmlElement) {
        BpmnElement element = ClassUtils.createNewInstance(type);

        try {
            populateElement(element, currentXmlElement);
            initializeElement(element);
        } catch (Throwable ex) {
            throw new IllegalStateException("[BPMN] Element creation failed", ex);
        }
        return element;
    }
}
