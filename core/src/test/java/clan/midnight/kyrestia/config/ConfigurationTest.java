package clan.midnight.kyrestia.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationTest {
    @Test
    void testDefaultConfiguration() {
        assertNotNull(Configuration.implementationAccessor);
    }
}