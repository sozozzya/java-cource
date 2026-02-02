package ru.senla.hotel.dao;

import ru.senla.hotel.exception.DAOException;

import java.util.List;

public interface GenericDAO<T, ID> {

    T findById(ID id) throws DAOException;

    List<T> findAll() throws DAOException;

    T save(T entity) throws DAOException;

    void update(T entity) throws DAOException;

    void deleteById(ID id) throws DAOException;
}
