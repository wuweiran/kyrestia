package clan.midnight.kyrestia.infra.spi;

import clan.midnight.kyrestia.infra.reflect.ClassUtils;

import java.util.*;

public class ExtensionScanner extends AbstractScanner {

    private final Map<String, Map<Class<?>, Integer>> scanResult;
    private final Map<String, Map<Class<?>, Object>> instances;

    public ExtensionScanner(String... packageNameList) {
        super(packageNameList);
        this.scanResult = new HashMap<>(16);
        this.instances = new HashMap<>(16);
    }

    @Override
    public void scan() {
        Set<Class<?>> classSet = scanClasses();
        for (Class<?> currentClazz : classSet) {
            boolean present = currentClazz.isAnnotationPresent(Extension.class);
            if (present) {
                Extension currentExtension = currentClazz.getAnnotation(Extension.class);
                String group = currentExtension.group();
                if (group != null) {
                    Map<Class<?>, Integer> groupBindings = scanResult.computeIfAbsent(group, k -> new HashMap<>(16));
                    groupBindings.put(currentClazz, currentExtension.priority());
                }
            }
        }
    }

    @Override
    public void clear() {
        scanResult.clear();
        instances.clear();
    }

    @SuppressWarnings("unchecked")
    public <T> T getExtension(String group, Class<T> expectedClazz) {
        Map<Class<?>, Integer> groupBindings = scanResult.get(group);
        if (groupBindings == null) {
            return null;
        }

        int priority = Integer.MIN_VALUE;
        Class<?> actualClazz = null;
        for (Map.Entry<Class<?>, Integer> entry : groupBindings.entrySet()) {
            if (expectedClazz.isAssignableFrom(entry.getKey()) && entry.getValue() > priority) {
                priority = entry.getValue();
                actualClazz = entry.getKey();
            }
        }

        if (actualClazz == null) {
            return null;
        }

        Map<Class<?>, Object> groupExtensions = instances.computeIfAbsent(group, k -> new HashMap<>(16));
        final Class<?> finalActualClazz = actualClazz;
        Object instance = groupExtensions.computeIfAbsent(finalActualClazz,
                k -> ClassUtils.createNewInstance(finalActualClazz));
        return (T) instance;
    }
}
