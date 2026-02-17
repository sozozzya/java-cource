package ru.senla.hotel.service;

import ru.senla.hotel.domain.model.Guest;

import java.util.List;
import java.util.Optional;

public interface GuestManager {

    Optional<Guest> findById(Long id);

    Optional<Guest> findGuestByName(String name);

    Guest getGuestByName(String name);

    Guest addGuest(Guest guest);

    void addGuestsBatch(List<Guest> guests);

    void exportGuestToCSV(String path);

    void importGuestFromCSV(String path);
}
