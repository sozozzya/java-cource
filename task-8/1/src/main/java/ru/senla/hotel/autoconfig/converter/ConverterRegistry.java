package ru.senla.hotel.autoconfig.converter;

import ru.senla.hotel.exception.config.ConversionException;

import java.util.EnumMap;
import java.util.Map;

public class ConverterRegistry {

    private final Map<ConverterType, ValueConverter<?>> converters = new EnumMap<>(ConverterType.class);

    public ConverterRegistry() {
        converters.put(ConverterType.STRING, DefaultConverters.STRING);
        converters.put(ConverterType.INT, DefaultConverters.INT);
        converters.put(ConverterType.BOOLEAN, DefaultConverters.BOOLEAN);
        converters.put(ConverterType.PATH, DefaultConverters.PATH);
    }

    @SuppressWarnings("unchecked")
    public <T> ValueConverter<T> get(ConverterType type) {
        ValueConverter<?> converter = converters.get(type);

        if (converter == null) {
            throw new ConversionException(
                    "No converter registered for type: " + type
            );
        }

        return (ValueConverter<T>) converter;
    }
}
