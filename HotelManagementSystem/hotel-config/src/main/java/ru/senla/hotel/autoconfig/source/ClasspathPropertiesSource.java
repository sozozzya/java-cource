package ru.senla.hotel.autoconfig.source;

import ru.senla.hotel.autoconfig.exception.ConfigException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ClasspathPropertiesSource implements PropertiesSource {

    private final Properties properties = new Properties();

    public ClasspathPropertiesSource(String resourceName) {
        try (InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream(resourceName)) {

            if (is == null) {
                throw new ConfigException("Config file not found: " + resourceName);
            }

            properties.load(is);
        } catch (IOException e) {
            throw new ConfigException("Failed to load config", e);
        }
    }

    @Override
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
