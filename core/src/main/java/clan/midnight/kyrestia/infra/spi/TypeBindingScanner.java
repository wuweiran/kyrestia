package clan.midnight.kyrestia.infra.spi;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TypeBindingScanner<T> extends AbstractScanner {
    private final Map<String, Class<? extends T>> scanResult;
    private final Class<T> expectedClazz;

    public TypeBindingScanner(Class<T> expectedClazz, String... packageNameList) {
        super(packageNameList);
        this.expectedClazz = expectedClazz;
        this.scanResult = new HashMap<>(32);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void scan() {
        Set<Class<?>> classSet = scanClasses();
        for (Class<?> currentClazz : classSet) {
            boolean present = currentClazz.isAnnotationPresent(TypeBinding.class);
            if (present && expectedClazz.isAssignableFrom(currentClazz)) {
                TypeBinding currentTypeBinding = currentClazz.getAnnotation(TypeBinding.class);
                if (currentTypeBinding.value() != null) {
                    scanResult.putIfAbsent(currentTypeBinding.value(), (Class<? extends T>) currentClazz);
                }
            }
        }
    }

    @Override
    public void clear() {
        scanResult.clear();
    }
    
    public Class<? extends T> getType(String key) {
        return scanResult.get(key);
    }
}
