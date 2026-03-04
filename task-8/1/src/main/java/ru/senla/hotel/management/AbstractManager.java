package ru.senla.hotel.management;

import ru.senla.hotel.exception.ManagerException;
import ru.senla.hotel.model.Identifiable;

import java.util.*;

public abstract class AbstractManager<T extends Identifiable> {

    protected final Map<Long, T> storage = new HashMap<>();
    private Long nextId = 1L;

    protected Long generateId() {
        return nextId++;
    }

    protected void syncNextId(Long usedId) {
        if (usedId >= nextId) {
            nextId = usedId + 1;
        }
    }

    public void save(T entity) {
        if (entity == null) {
            throw new ManagerException("Cannot save null entity.");
        }
        if (entity.getId() == null) {
            entity.setId(generateId());
        } else {
            syncNextId(entity.getId());
        }
        storage.put(entity.getId(), entity);
    }

    public Optional<T> findById(Long id) {
        if (id == null) {
            throw new ManagerException("Cannot search entity by null id.");
        }
        return Optional.ofNullable(storage.get(id));
    }

    public List<T> getAll() {
        return new ArrayList<>(storage.values());
    }

    public List<T> exportState() {
        return new ArrayList<>(storage.values());
    }

    public void importState(List<T> items) {
        storage.clear();
        nextId = 1L;
        if (items != null) {
            for (T item : items) {
                save(item);
            }
        }
    }
}
