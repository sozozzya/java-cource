package ru.senla.hotel.autoconfig.processor;

import ru.senla.hotel.autoconfig.annotation.ConfigProperty;
import ru.senla.hotel.autoconfig.converter.ConverterRegistry;
import ru.senla.hotel.autoconfig.converter.ConverterType;
import ru.senla.hotel.autoconfig.source.ClasspathPropertiesSource;
import ru.senla.hotel.autoconfig.source.PropertiesSource;
import ru.senla.hotel.autoconfig.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.nio.file.Path;

public class ConfigProcessor {

    private final ConverterRegistry converterRegistry = new ConverterRegistry();

    public void configure(Object target) {
        Class<?> clazz = target.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            ConfigProperty annotation = field.getAnnotation(ConfigProperty.class);
            if (annotation == null) continue;

            String fileName = annotation.configFileName().isEmpty()
                    ? "application.properties"
                    : annotation.configFileName();

            PropertiesSource source = new ClasspathPropertiesSource(fileName);

            String propertyName = annotation.propertyName().isEmpty()
                    ? ReflectionUtils.defaultPropertyName(field)
                    : annotation.propertyName();

            String rawValue = source.getProperty(propertyName);
            if (rawValue == null) continue;

            ConverterType type = annotation.type() == ConverterType.AUTO
                    ? inferType(field)
                    : annotation.type();

            Object value = converterRegistry.get(type).convert(rawValue);
            ReflectionUtils.setField(field, target, value);
        }
    }

    private ConverterType inferType(Field field) {
        Class<?> type = field.getType();

        if (type == int.class || type == Integer.class) return ConverterType.INT;
        if (type == boolean.class || type == Boolean.class) return ConverterType.BOOLEAN;
        if (type == Path.class) return ConverterType.PATH;

        return ConverterType.STRING;
    }
}
