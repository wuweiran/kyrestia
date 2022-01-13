package clan.midnight.kyrestia.bpmn.model;

import clan.midnight.kyrestia.bpmn.model.support.ElementInit;
import clan.midnight.kyrestia.bpmn.model.support.XmlReference;
import clan.midnight.kyrestia.infra.spi.TypeBinding;

@TypeBinding("bpmn:message")
public class Message extends IdBasedElement {
    public static final Message GLOBAL_DEFAULT_MESSAGE = new Message();
    static {
        GLOBAL_DEFAULT_MESSAGE.setId("__global_default_message");
        GLOBAL_DEFAULT_MESSAGE.name = "Global Default Message";
    }

    @XmlReference(type = XmlReference.Type.ATTRIBUTE, value = "name")
    private String name;

    @ElementInit
    public void init() {
        if (name == null) {
            name = "";
        }
    }

    public String getName() {
        return name;
    }
}
