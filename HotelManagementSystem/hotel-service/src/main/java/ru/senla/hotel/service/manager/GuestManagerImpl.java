package ru.senla.hotel.service.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.senla.hotel.repository.GuestRepository;
import ru.senla.hotel.repository.exception.RepositoryException;
import ru.senla.hotel.domain.model.Guest;
import ru.senla.hotel.service.GuestManager;
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

@Service
@Transactional
public class GuestManagerImpl implements GuestManager {

    private static final Logger log = LoggerFactory.getLogger(GuestManagerImpl.class);

    private final GuestRepository guestRepository;

    public GuestManagerImpl(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Guest> findById(Long id) {
        log.debug("Finding guest by id: {}", id);
        try {
            return Optional.ofNullable(guestRepository.findById(id));
        } catch (Exception e) {
            log.error("Failed to find guest by id: {}", id, e);
            throw new GuestException("Failed to find guest", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Guest> findGuestByName(String name) {
        log.debug("Finding guest by name: {}", name);
        try {
            return guestRepository.findByName(name);
        } catch (Exception e) {
            log.error("Failed to find guest by name: {}", name, e);
            throw new GuestException("Failed to find guest by name", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Guest getGuestByName(String name) {
        log.debug("Getting guest by name: {}", name);
        return findGuestByName(name)
                .orElseThrow(() -> {
                    log.warn("Guest not found: {}", name);
                    return new GuestNotFoundException(name);
                });
    }

    @Override
    public Guest addGuest(Guest guest) {
        log.info("Adding new guest with name='{}'", guest.getName());

        try {
            ensureGuestNotExists(guest);
            Guest saved = guestRepository.save(guest);

            log.info("Guest successfully added: id={}, name='{}'", saved.getId(), saved.getName());
            return saved;
        } catch (Exception e) {
            log.error("Failed to add guest with name='{}'", guest.getName(), e);
            throw new GuestException("Failed to add guest", e);
        }
    }

    @Override
    public void addGuestsBatch(List<Guest> guests) {
        log.info("Importing guests batch, size={}", guests.size());

        try {
            for (Guest guest : guests) {
                ensureGuestNotExists(guest);
                guestRepository.save(guest);
            }

            log.info("Guests batch successfully imported, count={}", guests.size());
        } catch (Exception e) {
            log.error("Failed to import guests batch", e);
            throw new GuestException("Failed to import guests batch", e);
        }
    }

    @Override
    public void exportGuestToCSV(String path) {
        log.info("Exporting guests to CSV, path='{}'", path);

        if (path == null || path.isBlank()) {
            log.warn("CSV export path is empty");
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
            guestRepository.findAll().forEach(g -> writer.println(g.toCsv()));

            log.info("Guests successfully exported to CSV: {}", file.getAbsolutePath());
        } catch (IOException | RepositoryException e) {
            log.error("Failed to export guests to CSV", e);
            throw new GuestCsvException("Failed to export guests: " + e.getMessage());
        }
    }

    @Override
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

    private void ensureGuestNotExists(Guest guest) {
        guestRepository.findByName(guest.getName())
                .ifPresent(g -> {
                    log.warn("Guest already exists: {}", guest.getName());
                    throw new GuestAlreadyExistsException(guest.getName());
                });
    }
}
