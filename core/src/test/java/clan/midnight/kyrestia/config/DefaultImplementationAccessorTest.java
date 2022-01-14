package clan.midnight.kyrestia.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultImplementationAccessorTest {
    @Test
    void testAccess() {
        ClassNameImplementationAccessor classNameImplementationAccessor = new ClassNameImplementationAccessor();
        Object implementation = classNameImplementationAccessor.access(TestImplementation.class.getName());

        assertNotNull(implementation);
    }
}