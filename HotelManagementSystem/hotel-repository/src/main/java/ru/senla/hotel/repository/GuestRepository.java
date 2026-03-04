package ru.senla.hotel.repository;

import ru.senla.hotel.domain.model.Guest;

import java.util.Optional;

public interface GuestRepository extends GenericRepository<Guest, Long> {

    Optional<Guest> findByName(String name);
}
