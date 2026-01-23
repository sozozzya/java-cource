package ru.senla.hotel.config;

import ru.senla.hotel.exception.ConfigurationException;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {

    public static ApplicationConfig load(String resourceName) {
        Properties props = new Properties();

        try (InputStream is = ConfigLoader.class
                .getClassLoader()
                .getResourceAsStream(resourceName)) {

            if (is == null) {
                throw new ConfigurationException("Config file not found in resources: " + resourceName);
            }

            props.load(is);

        } catch (Exception e) {
            throw new ConfigurationException("Failed to load config file: " + resourceName);
        }

        boolean roomStatusEnabled =
                Boolean.parseBoolean(
                        props.getProperty("room.status.change.enabled", "true")
                );

        int historySize =
                Integer.parseInt(
                        props.getProperty("room.history.size", "3")
                );

        return new ApplicationConfig(
                roomStatusEnabled,
                historySize
        );
    }
}
