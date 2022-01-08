package clan.midnight.kyrestia.bpmn.model.support;

import clan.midnight.kyrestia.bpmn.model.IdBasedElement;
import clan.midnight.kyrestia.infra.reflect.ClassUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ElementRegistry {
    private final Map<String, IdBasedElement> idBasedElements = new HashMap<>(32);
    private final Map<String, IdBasedElement> earlyIdBasedElements = new HashMap<>(16);
    private final Set<String> idBasedElementsCurrentlyInCreation = new HashSet<>(16);

    public void registerIdBasedElement(IdBasedElement element) throws IllegalStateException {
        if (element.getId() == null) {
            throw new IllegalArgumentException("[BPMN] Registering an invalid element with class: "
                    + element.getClass().getName());
        }
        IdBasedElement oldElement = idBasedElements.get(element.getId());
        if (oldElement != null) {
            throw new IllegalStateException("[BPMN] Could not register duplicated element with id: " + element.getId());
        } else {
            idBasedElements.put(element.getId(), element);
            earlyIdBasedElements.remove(element.getId());
        }
    }

    public IdBasedElement getIdBasedElement(String id) {
        IdBasedElement element = idBasedElements.get(id);
        if (element == null && isIdBasedElementCurrentlyInCreation(id)) {
            element = earlyIdBasedElements.get(id);
        }
        return element;
    }

    public <T extends IdBasedElement> IdBasedElement getIdBasedElement(String id, Class<T> clazz) {
        IdBasedElement element = idBasedElements.get(id);
        if (element == null) {
            element = earlyIdBasedElements.get(id);
            if (element == null && clazz != null) {
                element = ClassUtils.createNewInstance(clazz);
                element.setId(id);
                earlyIdBasedElements.put(id, element);
                idBasedElements.remove(id);
            }
        }
        return element;
    }

    public boolean isIdBasedElementCurrentlyInCreation(String id) {
        return idBasedElementsCurrentlyInCreation.contains(id);
    }

    public void beforeIdBasedElementCreation(String id) {
        idBasedElementsCurrentlyInCreation.add(id);
    }

    public void afterIdBaseElementCreation(String id) {
        idBasedElementsCurrentlyInCreation.remove(id);
    }
}
