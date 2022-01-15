package clan.midnight.kyrestia.infra.el.conversion;

public class StringCH implements ConversionHandler<String> {
    @Override
    public String convertFrom(Object in) {
        return String.valueOf(in);
    }

    @Override
    public boolean canConvertFrom(Class<?> clazz) {
        return true;
    }
}
