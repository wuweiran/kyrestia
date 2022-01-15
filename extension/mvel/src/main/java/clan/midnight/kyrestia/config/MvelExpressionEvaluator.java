package clan.midnight.kyrestia.config;

import clan.midnight.kyrestia.infra.spi.Extension;
import org.mvel2.MVEL;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Extension(group = Configuration.CONFIG_EXTENSION_GROUP, priority = 1)
public class MvelExpressionEvaluator implements ExpressionEvaluator {
    private static final ConcurrentHashMap<String, Serializable> EXP_CACHE = new ConcurrentHashMap<>(128);

    @Override
    public boolean evaluate(String expression, Map<String, Object> context) {
        Serializable compiledExp = compileExp(expression);
        return (Boolean) MVEL.executeExpression(compiledExp, context);
    }

    private Serializable compileExp(String expression) {
        return EXP_CACHE.computeIfAbsent(expression.trim(), MVEL::compileExpression);
    }
}
