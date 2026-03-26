package ru.senla.hotel.application.service;

import ru.senla.hotel.domain.entity.Favor;
import ru.senla.hotel.domain.enums.SortField;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface FavorService {

    Optional<Favor> findById(Long id);

    Optional<Favor> findFavorByName(String name);

    Favor getFavorByName(String name);

    List<Favor> getAvailableFavors(SortField field);

    Favor addFavor(Favor favor);

    void addFavorsBatch(List<Favor> favors);

    void changeFavorPrice(String favorName, BigDecimal newPrice);

    void exportFavorToCSV(String path);

    void importFavorFromCSV(String path);
}
