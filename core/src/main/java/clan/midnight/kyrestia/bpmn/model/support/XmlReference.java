package clan.midnight.kyrestia.bpmn.model.support;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
@Documented
public @interface XmlReference {
    enum Type {ATTRIBUTE_REF, CHILD_ELEMENT_REF, CHILD_ELEMENT, ATTRIBUTE, TEXT, ANCESTOR_ELEMENT}
    Type type();

    String value();
}
