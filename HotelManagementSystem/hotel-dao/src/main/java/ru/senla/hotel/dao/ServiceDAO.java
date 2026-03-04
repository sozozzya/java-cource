package ru.senla.hotel.dao;

import ru.senla.hotel.dao.exception.DAOException;
import ru.senla.hotel.model.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ServiceDAO extends GenericDAO<Service, Long> {

    Optional<Service> findByName(String name) throws DAOException;

    List<Service> findByDate(LocalDate date) throws DAOException;
}
