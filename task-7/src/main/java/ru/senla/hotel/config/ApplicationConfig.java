package ru.senla.hotel.config;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ApplicationConfig {

    private final boolean roomStatusChangeEnabled;
    private final int roomHistorySize;
    private final Path stateFilePath;

    public ApplicationConfig(boolean roomStatusChangeEnabled,
                             int roomHistorySize,
                             String stateFilePath) {
        if (roomHistorySize <= 0) {
            throw new IllegalArgumentException("room.history.size must be > 0");
        }
        this.roomStatusChangeEnabled = roomStatusChangeEnabled;
        this.roomHistorySize = roomHistorySize;
        this.stateFilePath = Paths.get(stateFilePath);
    }

    public Path getStateFilePath() {
        return stateFilePath;
    }

    public boolean isRoomStatusChangeEnabled() {
        return roomStatusChangeEnabled;
    }

    public int getRoomHistorySize() {
        return roomHistorySize;
    }
}
