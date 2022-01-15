package clan.midnight.kyrestia.infra.el.conversion;

import java.util.HashMap;
import java.util.Map;
import java.util.function.ToLongFunction;

public class LongCH implements ConversionHandler<Long> {

    private static final Map<Class<?>, ToLongFunction<Object>> CONVERTERS = new HashMap<>(10);
    static {
        CONVERTERS.put(Boolean.class, b -> Boolean.TRUE.equals(b) ? 1 : 0);
        CONVERTERS.put(Integer.class, i -> ((Integer) i).longValue());
    }

    @Override
    public Long convertFrom(Object in) {
        ToLongFunction<Object> function = CONVERTERS.get(in.getClass());
        if (function == null) {
            throw new IllegalArgumentException("[EL] Trying to convert to Long from inconvertible class: "
                    + in.getClass().getName());
        }
        return function.applyAsLong(in);
    }

    @Override
    public boolean canConvertFrom(Class<?> clazz) {
        return CONVERTERS.containsKey(clazz);
    }
}
