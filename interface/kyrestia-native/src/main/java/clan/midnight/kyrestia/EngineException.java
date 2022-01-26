package clan.midnight.kyrestia;

public class EngineException extends RuntimeException {
    public EngineException(String message) {
        super(message);
    }

    public EngineException(String message, Exception e) {
        super(message, e);
    }

    public EngineException(String message, Throwable e) {
        super(message, e);
    }
}
