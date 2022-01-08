package clan.midnight.kyrestia.bpmn;

public class ProcessDefinitionException extends RuntimeException {
    public ProcessDefinitionException(String msg) {
        super(msg);
    }

    public ProcessDefinitionException(String msg, Throwable t) {
        super(msg, t);
    }
}
