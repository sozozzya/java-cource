package ru.senla.hotel.storage;

import ru.senla.hotel.config.ApplicationConfig;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.exception.SerializationException;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class AppStateSaver {

    @Inject
    private ApplicationConfig config;

    public void save(AppState state) {
        Path path = config.getStateFilePath();

        try {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            try (ObjectOutputStream out =
                         new ObjectOutputStream(Files.newOutputStream(path))) {
                out.writeObject(state);
            }

        } catch (IOException e) {
            throw new SerializationException("Failed to save app state", e);
        }
    }
}
