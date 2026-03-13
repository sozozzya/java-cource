package ru.senla.hotel.infrastructure.repository;

import ru.senla.hotel.domain.entity.Favor;

import java.util.Optional;

public interface FavorRepository extends GenericRepository<Favor, Long> {

    Optional<Favor> findByName(String name);
}
