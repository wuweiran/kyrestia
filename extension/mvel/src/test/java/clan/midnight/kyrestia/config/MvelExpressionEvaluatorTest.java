package clan.midnight.kyrestia.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MvelExpressionEvaluatorTest {
    @Test
    void testExtension() {
        assertEquals(MvelExpressionEvaluator.class, Configuration.expressionEvaluator.getClass());
    }
}