package clan.midnight.kyrestia.infra.el.conversion;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class BooleanCH implements ConversionHandler<Boolean> {

    private static final Map<Class<?>, Predicate<Object>> CONVERTERS = new HashMap<>(10);
    static {
        CONVERTERS.put(Integer.class, i -> ((Integer) i) != 0);
    }

    @Override
    public Boolean convertFrom(Object in) {
        Predicate<Object> function = CONVERTERS.get(in.getClass());
        if (function == null) {
            throw new IllegalArgumentException("[EL] Trying to convert to Boolean from inconvertible class: "
                    + in.getClass().getName());
        }
        return function.test(in);
    }

    @Override
    public boolean canConvertFrom(Class<?> clazz) {
        return CONVERTERS.containsKey(clazz);
    }
}