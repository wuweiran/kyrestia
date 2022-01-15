package clan.midnight.kyrestia.config;

import clan.midnight.kyrestia.infra.el.SimpleProposition;
import clan.midnight.kyrestia.infra.spi.Extension;

import java.util.Map;

@Extension(group = Configuration.CONFIG_EXTENSION_GROUP)
public class SimplePropositionExpressionEvaluator implements ExpressionEvaluator {
    @Override
    public boolean evaluate(String expression, Map<String, Object> context) {
        return SimpleProposition.compileAndEvaluate(expression, context);
    }
}
