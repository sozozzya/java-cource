package ru.senla.hotel.dao;

import ru.senla.hotel.dao.exception.DAOException;
import ru.senla.hotel.model.Room;

import java.util.Optional;

public interface RoomDAO extends GenericDAO<Room, Long> {

    Optional<Room> findByNumber(int number) throws DAOException;
}
