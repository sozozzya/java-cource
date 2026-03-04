package ru.senla.hotel.config;

import ru.senla.hotel.autoconfig.annotation.ConfigProperty;
import ru.senla.hotel.autoconfig.converter.ConverterType;
import ru.senla.hotel.di.annotation.Component;

import java.nio.file.Path;

@Component
public class ApplicationConfig {

    @ConfigProperty(propertyName = "room.status.change.enabled")
    private boolean roomStatusChangeEnabled;

    @ConfigProperty(propertyName = "room.history.size")
    private int roomHistorySize;

    @ConfigProperty(propertyName = "app.state.file", type = ConverterType.PATH)
    private Path stateFilePath;

    public boolean isRoomStatusChangeEnabled() {
        return roomStatusChangeEnabled;
    }

    public int getRoomHistorySize() {
        return roomHistorySize;
    }

    public Path getStateFilePath() {
        return stateFilePath;
    }
}
