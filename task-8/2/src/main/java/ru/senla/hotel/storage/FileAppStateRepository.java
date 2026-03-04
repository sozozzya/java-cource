package ru.senla.hotel.storage;

import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;

@Component
public class FileAppStateRepository implements AppStateRepository {

    @Inject
    private AppStateLoader loader;

    @Inject
    private AppStateSaver saver;

    @Override
    public AppState load() {
        return loader.load();
    }

    @Override
    public void save(AppState state) {
        saver.save(state);
    }
}
