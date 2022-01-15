package clan.midnight.kyrestia.infra.el.conversion;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class FloatCH implements ConversionHandler<Float> {

    private static final Map<Class<?>, Function<Object, Float>> CONVERTERS = new HashMap<>(10);
    static {
        CONVERTERS.put(Integer.class, i -> ((Integer) i).floatValue());
    }

    @Override
    public Float convertFrom(Object in) {
        Function<Object, Float> function = CONVERTERS.get(in.getClass());
        if (function == null) {
            throw new IllegalArgumentException("[EL] Trying to convert to Float from inconvertible class: "
                    + in.getClass().getName());
        }
        return function.apply(in);
    }

    @Override
    public boolean canConvertFrom(Class<?> clazz) {
        return CONVERTERS.containsKey(clazz);
    }
}
