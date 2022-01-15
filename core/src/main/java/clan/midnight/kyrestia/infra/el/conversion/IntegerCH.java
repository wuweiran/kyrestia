package clan.midnight.kyrestia.infra.el.conversion;

import java.util.HashMap;
import java.util.Map;
import java.util.function.ToIntFunction;

public class IntegerCH implements ConversionHandler<Integer> {

    private static final Map<Class<?>, ToIntFunction<Object>> CONVERTERS = new HashMap<>(10);
    static {
        CONVERTERS.put(Boolean.class, b -> Boolean.TRUE.equals(b) ? 1 : 0);
    }

    @Override
    public Integer convertFrom(Object in) {
        ToIntFunction<Object> function = CONVERTERS.get(in.getClass());
        if (function == null) {
            throw new IllegalArgumentException("[EL] Trying to convert to Integer from inconvertible class: "
                    + in.getClass().getName());
        }
        return function.applyAsInt(in);
    }

    @Override
    public boolean canConvertFrom(Class<?> clazz) {
        return CONVERTERS.containsKey(clazz);
    }
}
