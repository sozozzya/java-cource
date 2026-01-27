package ru.senla.hotel.storage;

public interface AppStateRepository {

    AppState load();

    void save(AppState state);
}
