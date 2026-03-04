package ru.senla.hotel.repository;

import ru.senla.hotel.domain.model.Room;

import java.util.Optional;

public interface RoomRepository extends GenericRepository<Room, Long> {

    Optional<Room> findByNumber(int number);
}
