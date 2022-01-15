package clan.midnight.kyrestia.infra.el.conversion;

public interface ConversionHandler<T> {
    /**
     * Converts the passed argument to the type represented by the handler.
     *
     * @param in - the input type
     * @return - the converted type
     */
    T convertFrom(Object in);

    /**
     * This method is used to indicate to the runtime whether the handler knows how to convert
     * from the specified type.
     *
     * @param clazz - the source type
     * @return - true if the converter supports converting from the specified type.
     */
    boolean canConvertFrom(Class<?> clazz);
}
