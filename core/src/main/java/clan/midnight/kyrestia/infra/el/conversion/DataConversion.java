package clan.midnight.kyrestia.infra.el.conversion;

import clan.midnight.kyrestia.infra.reflect.ClassUtils;

import java.util.HashMap;
import java.util.Map;

public class DataConversion {
    private static final Map<Class<?>, ConversionHandler<?>> CONVERTERS = new HashMap<>(38);
    static {
        CONVERTERS.put(Integer.class, new IntegerCH());
        CONVERTERS.put(int.class, new IntegerCH());
        CONVERTERS.put(Long.class, new LongCH());
        CONVERTERS.put(long.class, new LongCH());
        CONVERTERS.put(Float.class, new FloatCH());
        CONVERTERS.put(float.class, new FloatCH());
        CONVERTERS.put(Double.class, new DoubleCH());
        CONVERTERS.put(double.class, new DoubleCH());
        CONVERTERS.put(Boolean.class, new BooleanCH());
        CONVERTERS.put(boolean.class, new BooleanCH());
        CONVERTERS.put(String.class, new StringCH());
    }

    private DataConversion() {}

    public static boolean canConvert(Class<?> toType, Class<?> convertFrom) {
        if (convertFrom.isAssignableFrom(toType) || areBoxingCompatible(toType, convertFrom)) return true;
        if (CONVERTERS.containsKey(toType)) {
            return CONVERTERS.get(toType).canConvertFrom(ClassUtils.toNonPrimitiveType(convertFrom));
        }
        return false;
    }

    private static boolean areBoxingCompatible(Class<?> c1, Class<?> c2) {
        return c1.isPrimitive() ? ClassUtils.isPrimitiveOf(c2, c1)
                : (c2.isPrimitive() && ClassUtils.isPrimitiveOf(c1, c2));
    }

    @SuppressWarnings("unchecked")
    public static <T> T convert(Object in, Class<T> toType) {
        if (in == null) {
            return null;
        }

        if (toType == in.getClass() || toType.isAssignableFrom(in.getClass())) {
            return (T) in;
        }

        ConversionHandler<?> h = CONVERTERS.get(toType);
        return (T) h.convertFrom(in);
    }
}
