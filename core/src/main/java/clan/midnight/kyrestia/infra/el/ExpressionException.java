package clan.midnight.kyrestia.infra.el;

public class ExpressionException extends RuntimeException {
    private final String token;

    public ExpressionException(String message, String token) {
        super(message);
        this.token = token;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + ", token: " + token;
    }
}
