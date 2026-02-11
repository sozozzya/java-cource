package ru.senla.hotel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.hotel.dao.GuestDAO;
import ru.senla.hotel.db.ConnectionManager;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.dao.exception.DAOException;
import ru.senla.hotel.model.Guest;
import ru.senla.hotel.service.exception.guest.GuestAlreadyExistsException;
import ru.senla.hotel.service.exception.guest.GuestCsvException;
import ru.senla.hotel.service.exception.guest.GuestException;
import ru.senla.hotel.service.exception.guest.GuestNotFoundException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class GuestManager {

    @Inject
    private GuestDAO guestDAO;

    @Inject
    private ConnectionManager connectionManager;

    private static final Logger log = LoggerFactory.getLogger(GuestManager.class);

    public Optional<Guest> findById(Long id) {
        log.info("Searching guest by id={}", id);
        return Optional.ofNullable(guestDAO.findById(id));
    }

    public Optional<Guest> findGuestByName(String name) {
        log.info("Searching guest by name='{}'", name);
        return guestDAO.findByName(name);
    }

    public Guest getGuestByName(String name) {
        log.info("Getting guest by name='{}'", name);
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
        log.info("Adding new guest with name='{}'", guest.getName());

        try {
            ensureGuestNotExists(guest);

            Guest saved = guestDAO.save(guest);
            connectionManager.commit();

            log.info("Guest successfully added: id={}, name='{}'", saved.getId(), saved.getName());

            return saved;
        } catch (Exception e) {
            connectionManager.rollback();
            log.error("Failed to add guest with name='{}'", guest.getName(), e);
            throw new GuestException("Failed to add guest", e);
        }
    }

    public void addGuestsBatch(List<Guest> guests) {
        log.info("Importing guests batch, size={}", guests.size());

        try {
            for (Guest guest : guests) {
                ensureGuestNotExists(guest);
                guestDAO.save(guest);
            }

            connectionManager.commit();
            log.info("Guests batch successfully imported, count={}", guests.size());
        } catch (Exception e) {
            connectionManager.rollback();
            log.error("Failed to import guests batch", e);
            throw new GuestException("Failed to import guests batch", e);
        }
    }

    public void exportGuestToCSV(String path) {
        log.info("Exporting guests to CSV, path='{}'", path);

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

            log.info("Guests successfully exported to CSV: {}", file.getAbsolutePath());
        } catch (IOException | DAOException e) {
            log.error("Failed to export guests to CSV", e);
            throw new GuestCsvException("Failed to export guests: " + e.getMessage());
        }
    }

    public void importGuestFromCSV(String path) {
        log.info("Importing guests from CSV, path='{}'", path);

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine();
            List<Guest> guests = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                guests.add(Guest.fromCsv(line));
            }
            addGuestsBatch(guests);

            log.info("Guests successfully imported from CSV, count={}", guests.size());
        } catch (Exception e) {
            log.error("Failed to import guests from CSV", e);
            throw new GuestCsvException("Failed to import guests: " + e.getMessage());
        }
    }
}
