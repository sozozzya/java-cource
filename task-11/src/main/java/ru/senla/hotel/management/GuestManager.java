package ru.senla.hotel.management;

import ru.senla.hotel.dao.GuestDAO;
import ru.senla.hotel.db.ConnectionManager;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.exception.DAOException;
import ru.senla.hotel.exception.guest.*;
import ru.senla.hotel.model.Guest;

import java.io.*;
import java.util.*;

@Component
public class GuestManager {

    @Inject
    private GuestDAO guestDAO;

    @Inject
    private ConnectionManager connectionManager;

    public Optional<Guest> findById(Long id) {
        return Optional.ofNullable(guestDAO.findById(id));
    }

    public Optional<Guest> findGuestByName(String name) {
        return guestDAO.findByName(name);
    }

    public Guest getGuestByName(String name) {
        return findGuestByName(name)
                .orElseThrow(() -> new GuestNotFoundException(name));
    }

    private void ensureGuestNotExists(Guest guest) {
        guestDAO.findByName(guest.getName())
                .ifPresent(g -> {
                    throw new GuestAlreadyExistsException(guest.getName());
                });
    }

    public Guest addGuest(Guest guest) {
        try {
            ensureGuestNotExists(guest);

            Guest saved = guestDAO.save(guest);
            connectionManager.commit();
            return saved;

        } catch (Exception e) {
            connectionManager.rollback();
            throw new GuestException("Failed to add guest", e);
        }
    }

    public void addGuestsBatch(List<Guest> guests) {
        try {
            for (Guest guest : guests) {
                ensureGuestNotExists(guest);
                guestDAO.save(guest);
            }

            connectionManager.commit();

        } catch (Exception e) {
            connectionManager.rollback();
            throw new GuestException("Failed to import guests batch", e);
        }
    }

    public void exportGuestToCSV(String path) {
        if (path == null || path.isBlank()) {
            throw new GuestCsvException("CSV export path cannot be empty.");
        }

        File file = new File(path);

        if (file.isDirectory()) {
            file = new File(file, "guests.csv");
        } else {
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                throw new GuestCsvException(
                        "Invalid file format. CSV file expected: " + file.getName()
                );
            }
        }

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("id;name");
            guestDAO.findAll().forEach(g -> writer.println(g.toCsv()));
        } catch (IOException | DAOException e) {
            throw new GuestCsvException("Failed to export guests: " + e.getMessage());
        }
    }

    public void importGuestFromCSV(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine();
            List<Guest> guests = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                guests.add(Guest.fromCsv(line));
            }
            addGuestsBatch(guests);
        } catch (Exception e) {
            throw new GuestCsvException("Failed to import guests: " + e.getMessage());
        }
    }
}
