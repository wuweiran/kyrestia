package clan.midnight.kyrestia.config;

import clan.midnight.kyrestia.infra.spi.Extension;

@Extension(group = Configuration.CONFIG_EXTENSION_GROUP)
public class ClassNameImplementationAccessor implements ImplementationAccessor {
    @Override
    public Object access(String identifier) {
        try {
            return Class.forName(identifier);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
