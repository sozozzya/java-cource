package ru.senla.hotel.repository;

import java.util.List;

public interface GenericRepository<T, ID> {

    T findById(ID id);

    List<T> findAll();

    T save(T entity);

    void update(T entity);
}
