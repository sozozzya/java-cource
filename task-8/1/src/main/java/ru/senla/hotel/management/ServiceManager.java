package ru.senla.hotel.management;

import ru.senla.hotel.exception.guest.GuestCsvException;
import ru.senla.hotel.exception.service.InvalidServicePriceException;
import ru.senla.hotel.exception.service.ServiceAlreadyExistsException;
import ru.senla.hotel.exception.service.ServiceCsvException;
import ru.senla.hotel.exception.service.ServiceNotFoundException;
import ru.senla.hotel.model.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

public class ServiceManager extends AbstractManager<Service> {

    public void changeServicePrice(String serviceName, double newPrice) {
        if (newPrice < 0) {
            throw new InvalidServicePriceException(newPrice);
        }
        Service service = getServiceByName(serviceName);
        service.setPrice(newPrice);
    }

    public Optional<Service> findServiceByName(String name) {
        return storage.values().stream()
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public Service getServiceByName(String name) {
        return findServiceByName(name).orElseThrow(() -> new ServiceNotFoundException(name));
    }

    public void addService(Service service) {
        boolean exists = storage.values().stream()
                .anyMatch(s -> s.getName().equalsIgnoreCase(service.getName()));
        if (exists) {
            throw new ServiceAlreadyExistsException(service.getName());
        }
        save(service);
    }

    public List<Service> getAvailableServices() {
        return getAll();
    }

    public void exportServiceToCSV(String path) {
        if (path == null || path.isBlank()) {
            throw new GuestCsvException("CSV export path cannot be empty.");
        }

        File file = new File(path);

        if (file.isDirectory()) {
            file = new File(file, "services.csv");
        } else {
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                throw new GuestCsvException(
                        "Invalid file format. CSV file expected: " + file.getName()
                );
            }
        }

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("id;name;price;date");
            storage.values().forEach(s -> writer.println(s.toCsv()));
        } catch (Exception e) {
            throw new ServiceCsvException("Failed to export services: " + e.getMessage());
        }
    }

    public void importServiceFromCSV(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                Service importedService = Service.fromCsv(line);
                addService(importedService);
            }
        } catch (Exception e) {
            throw new ServiceCsvException("Failed to import services: " + e.getMessage());
        }
    }

    public List<Service> exportStateForAppState() {
        return exportState();
    }

    public void importStateFromAppState(List<Service> services) {
        importState(services);
    }
}
