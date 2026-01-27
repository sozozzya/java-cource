package ru.senla.hotel.storage;

import ru.senla.hotel.exception.SerializationException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class AppStateLoader {

    public AppState load(Path path) {
        if (!Files.exists(path)) {
            return new AppState();
        }

        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(path))) {
            return (AppState) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new SerializationException("Failed to load app state", e);
        }
    }
}
