package ru.senla.hotel.service;

import ru.senla.hotel.domain.model.HotelService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ServiceManager {

    Optional<HotelService> findById(Long id);

    Optional<HotelService> findServiceByName(String name);

    HotelService getServiceByName(String name);

    List<HotelService> getAvailableServices();

    HotelService addService(HotelService service);

    void addServicesBatch(List<HotelService> services);

    void changeServicePrice(String serviceName, BigDecimal newPrice);

    void exportServiceToCSV(String path);

    void importServiceFromCSV(String path);
}
