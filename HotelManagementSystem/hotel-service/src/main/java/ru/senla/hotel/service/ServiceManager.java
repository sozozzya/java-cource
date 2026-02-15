package ru.senla.hotel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.hotel.dao.ServiceDAO;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.dao.exception.DAOException;
import ru.senla.hotel.model.Service;
import ru.senla.hotel.service.exception.service.ServiceCsvException;
import ru.senla.hotel.service.exception.service.InvalidServicePriceException;
import ru.senla.hotel.service.exception.service.ServiceAlreadyExistsException;
import ru.senla.hotel.service.exception.service.ServiceNotFoundException;
import ru.senla.hotel.service.exception.service.ServiceException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ServiceManager {

    @Inject
    private ServiceDAO serviceDAO;

    private static final Logger log = LoggerFactory.getLogger(ServiceManager.class);

    public Optional<Service> findById(Long id) {
        log.info("Searching service by id={}", id);
        return Optional.ofNullable(serviceDAO.findById(id));
    }

    public Optional<Service> findServiceByName(String name) {
        log.info("Searching service by name='{}'", name);
        return serviceDAO.findByName(name);
    }

    public Service getServiceByName(String name) {
        log.info("Getting service by name='{}'", name);
        return findServiceByName(name)
                .orElseThrow(() -> new ServiceNotFoundException(name));
    }

    public List<Service> getAvailableServices() {
        log.info("Fetching all available services");
        return serviceDAO.findAll();
    }

    private void ensureServiceNotExists(Service service) {
        try {
            serviceDAO.findByName(service.getName())
                    .ifPresent(s -> {
                        throw new ServiceAlreadyExistsException(service.getName());
                    });
        } catch (DAOException e) {
            throw new ServiceException("Failed to validate service uniqueness", e);
        }
    }

    public Service addService(Service service) {
        log.info("Adding new service with name='{}'", service.getName());

        try {
            ensureServiceNotExists(service);

            Service saved = serviceDAO.save(service);

            log.info("Service successfully added: id={}, name='{}'", saved.getId(), saved.getName());
            return saved;
        } catch (Exception e) {
            log.error("Failed to add service with name='{}'", service.getName(), e);
            throw new ServiceException("Failed to add service", e);
        }
    }

    public void addServicesBatch(List<Service> services) {
        log.info("Importing services batch, size={}", services.size());

        try {
            for (Service service : services) {
                ensureServiceNotExists(service);
                serviceDAO.save(service);
            }

            log.info("Services batch successfully imported, count={}", services.size());
        } catch (Exception e) {
            log.error("Failed to import services batch", e);
            throw new ServiceException("Failed to import services batch", e);
        }
    }

    public void changeServicePrice(String serviceName, BigDecimal newPrice) {
        log.info("Changing service price: name='{}', newPrice={}", serviceName, newPrice);

        if (newPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidServicePriceException(newPrice);
        }

        try {
            Service service = getServiceByName(serviceName);
            service.setPrice(newPrice);

            serviceDAO.update(service);

            log.info("Service price successfully changed: name='{}', price={}",
                    serviceName, newPrice);
        } catch (Exception e) {
            log.error("Failed to change service price: name='{}'", serviceName, e);
            throw new ServiceException("Failed to change service price", e);
        }
    }

    public void exportServiceToCSV(String path) {
        log.info("Exporting services to CSV, path='{}'", path);

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

            log.info("Services successfully exported to CSV: {}", file.getAbsolutePath());
        } catch (IOException | DAOException e) {
            log.error("Failed to export services to CSV", e);
            throw new ServiceCsvException("Failed to export services: " + e.getMessage());
        }
    }

    public void importServiceFromCSV(String path) {
        log.info("Importing services from CSV, path='{}'", path);

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine();
            List<Service> services = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                services.add(Service.fromCsv(line));
            }
            addServicesBatch(services);

            log.info("Services successfully imported from CSV, count={}", services.size());
        } catch (Exception e) {
            log.error("Failed to import services from CSV", e);
            throw new ServiceCsvException("Failed to import services: " + e.getMessage());
        }
    }
}
