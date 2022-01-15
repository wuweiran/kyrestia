package clan.midnight.kyrestia.infra.el;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class SimplePropositionTest {
    @Test
    void testCompileAndEvaluateWithConstants() {
        boolean result = SimpleProposition.compileAndEvaluate("1 == 1", null);
        assertTrue(result);
    }

    @Test
    void testCompileAndEvaluateWithContext() {
        boolean result = SimpleProposition.compileAndEvaluate("test != 1", Collections.singletonMap("test", 1L));
        assertFalse(result);
    }
}