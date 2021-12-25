package clan.midnight.kyrestia.infra.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ClassUtils {
    private ClassUtils() {}

    public static Class<?> loadClass(String className) {
        try {
            return Class.forName(className, true, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            // fall back
            try {
                return Class.forName(className, true, ClassUtils.class.getClassLoader());
            } catch (ClassNotFoundException ee) {
                throw new IllegalArgumentException("[Reflect] Unable to load class: " + className, e);
            }
        }
    }

    public static <T> T createNewInstance(Class<? extends T>  clazz) {
        try {
            Constructor<? extends T> constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("[Reflect] No constructor found for class: " + clazz.getName(), e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("[Reflect] Unable to construct object for class: " + clazz.getName(), e);
        }
    }
}
