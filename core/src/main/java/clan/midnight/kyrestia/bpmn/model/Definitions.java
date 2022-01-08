package clan.midnight.kyrestia.bpmn.model;

import clan.midnight.kyrestia.bpmn.model.support.XmlReference;
import clan.midnight.kyrestia.infra.spi.TypeBinding;

import java.util.ArrayList;
import java.util.List;

@TypeBinding("bpmn:definitions")
public class Definitions extends IdBasedElement {
    @XmlReference(type = XmlReference.Type.CHILD_ELEMENT, value = "bpmn:process")
    private final ArrayList<Process> processList = new ArrayList<>(2);

    public List<Process> getProcesses() {
        return processList;
    }
}
