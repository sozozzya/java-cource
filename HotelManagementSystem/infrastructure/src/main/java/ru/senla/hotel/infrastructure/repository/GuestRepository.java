package ru.senla.hotel.infrastructure.repository;

import ru.senla.hotel.domain.entity.Guest;

import java.util.Optional;

public interface GuestRepository extends GenericRepository<Guest, Long> {

    Optional<Guest> findByName(String name);
}
