package ru.senla.hotel.application.service;

import ru.senla.hotel.domain.entity.Guest;

import java.util.List;
import java.util.Optional;

public interface GuestService {

    Optional<Guest> findById(Long id);

    Optional<Guest> findGuestByName(String name);

    List<Guest> getAllGuests();

    Guest getGuestByName(String name);

    Guest addGuest(Guest guest);

    void addGuestsBatch(List<Guest> guests);

    void exportGuestToCSV(String path);

    void importGuestFromCSV(String path);
}
