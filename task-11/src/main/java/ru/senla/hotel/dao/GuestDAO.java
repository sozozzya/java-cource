package ru.senla.hotel.dao;

import ru.senla.hotel.exception.DAOException;
import ru.senla.hotel.model.Guest;

import java.util.Optional;

public interface GuestDAO extends GenericDAO<Guest, Long> {

    Optional<Guest> findByName(String name) throws DAOException;
}
