package clan.midnight.kyrestia.config;

import clan.midnight.kyrestia.infra.reflect.ClassUtils;
import clan.midnight.kyrestia.infra.spi.Extension;

@Extension(group = Configuration.CONFIG_EXTENSION_GROUP)
public class ClassNameImplementationAccessor implements ImplementationAccessor {
    @Override
    public Object access(String identifier) {
        try {
            Class<?> clazz = ClassUtils.loadClass(identifier);
            return ClassUtils.createNewInstance(clazz);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
