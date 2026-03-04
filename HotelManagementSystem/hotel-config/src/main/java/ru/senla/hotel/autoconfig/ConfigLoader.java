package ru.senla.hotel.autoconfig;

import ru.senla.hotel.autoconfig.processor.ConfigProcessor;

public final class ConfigLoader {

    private ConfigLoader() {
    }

    public static <T> T load(Class<T> configClass) {
        try {
            T instance = configClass.getDeclaredConstructor().newInstance();

            ConfigProcessor processor = new ConfigProcessor();
            processor.process(instance);

            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config for class: " + configClass.getName(), e);
        }
    }
}
