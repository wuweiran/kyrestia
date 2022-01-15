package clan.midnight.kyrestia.infra.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ClassUtils {
    private ClassUtils() {
    }

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

    public static <T> T createNewInstance(Class<? extends T> clazz) {
        try {
            Constructor<? extends T> constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("[Reflect] No constructor found for class: " + clazz.getName(), e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("[Reflect] Unable to construct object for class: " + clazz.getName(), e);
        }
    }

    public static Class<?> toNonPrimitiveType(Class<?> c) {
        if (!c.isPrimitive()) {
            return c;
        }
        if (c == int.class) {
            return Integer.class;
        }
        if (c == long.class) {
            return Long.class;
        }
        if (c == double.class) {
            return Double.class;
        }
        if (c == float.class) {
            return Float.class;
        }
        if (c == short.class) {
            return Short.class;
        }
        if (c == byte.class) {
            return Byte.class;
        }
        if (c == char.class) {
            return Character.class;
        }
        return Boolean.class;
    }

    public static boolean isPrimitiveOf(Class<?> boxed, Class<?> primitive) {
        if (primitive == int.class) {
            return boxed == Integer.class;
        }
        if (primitive == long.class) {
            return boxed == Long.class;
        }
        if (primitive == double.class) {
            return boxed == Double.class;
        }
        if (primitive == float.class) {
            return boxed == Float.class;
        }
        if (primitive == short.class) {
            return boxed == Short.class;
        }
        if (primitive == byte.class) {
            return boxed == Byte.class;
        }
        if (primitive == char.class) {
            return boxed == Character.class;
        }
        if (primitive == boolean.class) {
            return boxed == Boolean.class;
        }
        return false;
    }
}
