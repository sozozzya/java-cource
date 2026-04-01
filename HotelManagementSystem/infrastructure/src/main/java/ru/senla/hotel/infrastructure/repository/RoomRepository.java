package ru.senla.hotel.infrastructure.repository;

import ru.senla.hotel.domain.entity.Room;

import java.util.Optional;

public interface RoomRepository extends GenericRepository<Room, Long> {

    Optional<Room> findByNumber(int number);
}
