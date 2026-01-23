package ru.senla.hotel.config;

public class ApplicationConfig {

    private final boolean roomStatusChangeEnabled;
    private final int roomHistorySize;

    public ApplicationConfig(boolean roomStatusChangeEnabled,
                             int roomHistorySize) {

        if (roomHistorySize <= 0) {
            throw new IllegalArgumentException("room.history.size must be > 0");
        }

        this.roomStatusChangeEnabled = roomStatusChangeEnabled;
        this.roomHistorySize = roomHistorySize;
    }

    public boolean isRoomStatusChangeEnabled() {
        return roomStatusChangeEnabled;
    }

    public int getRoomHistorySize() {
        return roomHistorySize;
    }
}
