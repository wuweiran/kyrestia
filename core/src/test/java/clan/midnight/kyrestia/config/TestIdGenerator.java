package clan.midnight.kyrestia.config;

import clan.midnight.kyrestia.infra.spi.Extension;

import java.util.UUID;

@Extension(group = Configuration.CONFIG_EXTENSION_GROUP, priority = 99)
public class TestIdGenerator implements IdGenerator {
    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
