package clan.midnight.kyrestia.infra.el.conversion;

import java.util.HashMap;
import java.util.Map;
import java.util.function.ToDoubleFunction;

public class DoubleCH implements ConversionHandler<Double> {

    private static final Map<Class<?>, ToDoubleFunction<Object>> CONVERTERS = new HashMap<>(10);
    static {
        CONVERTERS.put(Integer.class, i -> ((Integer) i).doubleValue());
        CONVERTERS.put(Long.class, l -> ((Long) l).doubleValue());
        CONVERTERS.put(Float.class, f -> ((Float) f).doubleValue());
    }

    @Override
    public Double convertFrom(Object in) {
        ToDoubleFunction<Object> function = CONVERTERS.get(in.getClass());
        if (function == null) {
            throw new IllegalArgumentException("[EL] Trying to convert to Float from inconvertible class: "
                    + in.getClass().getName());
        }
        return function.applyAsDouble(in);
    }

    @Override
    public boolean canConvertFrom(Class<?> clazz) {
        return CONVERTERS.containsKey(clazz);
    }
}
