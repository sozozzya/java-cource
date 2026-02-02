package ru.senla.hotel.management;

import ru.senla.hotel.dao.ServiceDAO;
import ru.senla.hotel.db.ConnectionManager;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.exception.DAOException;
import ru.senla.hotel.exception.guest.GuestCsvException;
import ru.senla.hotel.exception.service.*;
import ru.senla.hotel.model.Service;

import java.io.*;
import java.util.*;

@Component
public class ServiceManager {

    @Inject
    private ServiceDAO serviceDAO;

    @Inject
    private ConnectionManager connectionManager;

    public Optional<Service> findById(Long id) {
        return Optional.ofNullable(serviceDAO.findById(id));
    }

    public Optional<Service> findServiceByName(String name) {
        return serviceDAO.findByName(name);
    }

    public Service getServiceByName(String name) {
        return findServiceByName(name)
                .orElseThrow(() -> new ServiceNotFoundException(name));
    }

    public List<Service> getAvailableServices() {
        return serviceDAO.findAll();
    }

    private void ensureServiceNotExists(Service service) {
        serviceDAO.findByName(service.getName())
                .ifPresent(s -> {
                    throw new ServiceAlreadyExistsException(service.getName());
                });
    }

    public Service addService(Service service) {
        try {
            ensureServiceNotExists(service);

            Service saved = serviceDAO.save(service);
            connectionManager.commit();
            return saved;

        } catch (Exception e) {
            connectionManager.rollback();
            throw new ServiceException("Failed to add service", e);
        }
    }

    public void addServicesBatch(List<Service> services) {
        try {
            for (Service service : services) {
                ensureServiceNotExists(service);
                serviceDAO.save(service);
            }

            connectionManager.commit();

        } catch (Exception e) {
            connectionManager.rollback();
            throw new ServiceException("Failed to import services batch", e);
        }
    }

    public void changeServicePrice(String serviceName, double newPrice) {
        if (newPrice < 0) {
            throw new InvalidServicePriceException(newPrice);
        }

        try {
            Service service = getServiceByName(serviceName);
            service.setPrice(newPrice);

            serviceDAO.update(service);
            connectionManager.commit();

        } catch (Exception e) {
            connectionManager.rollback();
            throw new ServiceException("Failed to change service price", e);
        }
    }

    public void exportServiceToCSV(String path) {
        if (path == null || path.isBlank()) {
            throw new ServiceCsvException("CSV export path cannot be empty.");
        }

        File file = new File(path);

        if (file.isDirectory()) {
            file = new File(file, "services.csv");
        } else {
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                throw new ServiceCsvException(
                        "Invalid file format. CSV file expected: " + file.getName()
                );
            }
        }

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("id;name;price;date");
            serviceDAO.findAll().forEach(s -> writer.println(s.toCsv()));
        } catch (IOException | DAOException e) {
            throw new ServiceCsvException("Failed to export services: " + e.getMessage());
        }
    }

    public void importServiceFromCSV(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine();
            List<Service> services = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                services.add(Service.fromCsv(line));
            }
            addServicesBatch(services);
        } catch (Exception e) {
            throw new ServiceCsvException("Failed to import services: " + e.getMessage());
        }
    }
}
