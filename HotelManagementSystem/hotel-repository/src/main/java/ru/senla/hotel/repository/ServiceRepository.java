package ru.senla.hotel.repository;

import ru.senla.hotel.domain.model.HotelService;

import java.util.Optional;

public interface ServiceRepository extends GenericRepository<HotelService, Long> {

    Optional<HotelService> findByName(String name);
}
