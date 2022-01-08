package clan.midnight.kyrestia.bpmn.model;

public abstract class IdBasedElement {
    protected String id;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
