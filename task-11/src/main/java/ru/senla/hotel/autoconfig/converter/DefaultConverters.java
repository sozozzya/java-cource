package ru.senla.hotel.autoconfig.converter;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class DefaultConverters {

    public static final ValueConverter<String> STRING =
            value -> value;

    public static final ValueConverter<Integer> INT =
            Integer::parseInt;

    public static final ValueConverter<Boolean> BOOLEAN =
            Boolean::parseBoolean;

    public static final ValueConverter<Path> PATH =
            Paths::get;
}
