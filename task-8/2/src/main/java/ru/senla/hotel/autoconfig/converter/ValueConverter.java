package ru.senla.hotel.autoconfig.converter;

public interface ValueConverter<T> {
    T convert(String value);
}
