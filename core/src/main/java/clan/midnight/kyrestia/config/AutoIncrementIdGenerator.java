package clan.midnight.kyrestia.config;

import clan.midnight.kyrestia.infra.spi.Extension;

import java.util.concurrent.atomic.AtomicLong;

@Extension(group = Configuration.CONFIG_EXTENSION_GROUP)
public class AutoIncrementIdGenerator implements IdGenerator {
    private final AtomicLong atomicLong = new AtomicLong();

    @Override
    public String generate() {
        return Long.toString(atomicLong.incrementAndGet());
    }
}
