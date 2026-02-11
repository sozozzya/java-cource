package ru.senla.hotel.storage;

import ru.senla.hotel.exception.SerializationException;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class AppStateSaver {

    public void save(AppState state, Path path) {
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(path))) {
            out.writeObject(state);
        } catch (IOException e) {
            throw new SerializationException("Failed to save app state", e);
        }
    }
}
