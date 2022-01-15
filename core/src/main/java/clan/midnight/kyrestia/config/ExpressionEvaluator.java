package clan.midnight.kyrestia.config;

import java.util.Map;

public interface ExpressionEvaluator {
    boolean evaluate(String expression, Map<String, Object> context);
}
