package ru.senla.hotel.management;

import ru.senla.hotel.exception.guest.*;
import ru.senla.hotel.model.Guest;

import java.io.*;
import java.util.*;

public class GuestManager extends AbstractManager<Guest> {

    public Optional<Guest> findGuestByName(String name) {
        return storage.values().stream()
                .filter(g -> g.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public Guest getGuestByName(String name) {
        return findGuestByName(name)
                .orElseThrow(() -> new GuestNotFoundException(name));
    }

    public void addGuest(Guest guest) {
        boolean exists = storage.values().stream()
                .anyMatch(g -> g.getName().equalsIgnoreCase(guest.getName()));

        if (exists) {
            throw new GuestAlreadyExistsException(guest.getName());
        }

        save(guest);
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
            storage.values().forEach(g -> writer.println(g.toCsv()));
        } catch (Exception e) {
            throw new GuestCsvException("Failed to export guests: " + e.getMessage());
        }
    }

    public void importGuestFromCSV(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                Guest importedGuest = Guest.fromCsv(line);
                addGuest(importedGuest);
            }
        } catch (Exception e) {
            throw new GuestCsvException("Failed to import guests: " + e.getMessage());
        }
    }

    public List<Guest> exportStateForAppState() {
        return exportState();
    }

    public void importStateFromAppState(List<Guest> guests) {
        importState(guests);
    }
}
