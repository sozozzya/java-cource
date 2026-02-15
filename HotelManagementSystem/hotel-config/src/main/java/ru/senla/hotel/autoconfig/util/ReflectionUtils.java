package ru.senla.hotel.autoconfig.util;

import ru.senla.hotel.autoconfig.exception.ConfigException;

import java.lang.reflect.Field;

public final class ReflectionUtils {

    public static void setField(Field field, Object target, Object value) {
        try {
            field.setAccessible(true);
            field.set(target, value);
        } catch (IllegalAccessException e) {
            throw new ConfigException("Failed to set field: " + field.getName(), e);
        }
    }

    public static String defaultPropertyName(Field field) {
        return field.getDeclaringClass().getSimpleName()
                + "." + field.getName();
    }
}
