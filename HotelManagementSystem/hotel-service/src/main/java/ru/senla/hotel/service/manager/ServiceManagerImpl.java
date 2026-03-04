package ru.senla.hotel.service.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.senla.hotel.repository.ServiceRepository;
import ru.senla.hotel.repository.exception.RepositoryException;
import ru.senla.hotel.domain.model.HotelService;
import ru.senla.hotel.service.ServiceManager;
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

@Service
@Transactional
public class ServiceManagerImpl implements ServiceManager {

    private static final Logger log = LoggerFactory.getLogger(ServiceManagerImpl.class);

    private final ServiceRepository serviceRepository;

    public ServiceManagerImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public Optional<HotelService> findById(Long id) {
        log.debug("Finding service by id: {}", id);
        try {
            return Optional.ofNullable(serviceRepository.findById(id));
        } catch (Exception e) {
            log.error("Failed to find service by id: {}", id, e);
            throw new ServiceException("Failed to find service", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HotelService> findServiceByName(String name) {
        log.debug("Finding service by name: {}", name);
        try {
            return serviceRepository.findByName(name);
        } catch (Exception e) {
            log.error("Failed to find service by name: {}", name, e);
            throw new ServiceException("Failed to find service by name", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public HotelService getServiceByName(String name) {
        log.debug("Getting service by name: {}", name);

        return findServiceByName(name)
                .orElseThrow(() -> {
                    log.warn("Service not found: {}", name);
                    return new ServiceNotFoundException(name);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<HotelService> getAvailableServices() {
        log.debug("Fetching all available services");

        try {
            return serviceRepository.findAll();
        } catch (Exception e) {
            log.error("Failed to fetch services", e);
            throw new ServiceException("Failed to fetch services", e);
        }
    }

    @Override
    public HotelService addService(HotelService service) {
        log.info("Adding service: name={}, price={}", service.getName(), service.getPrice());

        try {
            ensureServiceNotExists(service);

            HotelService saved = serviceRepository.save(service);

            log.info("Service successfully added: id={}, name='{}'", saved.getId(), saved.getName());
            return saved;
        } catch (Exception e) {
            log.error("Failed to add service with name='{}'", service.getName(), e);
            throw new ServiceException("Failed to add service", e);
        }
    }

    @Override
    public void addServicesBatch(List<HotelService> services) {
        log.info("Importing services batch, size={}", services.size());

        try {
            for (HotelService service : services) {
                ensureServiceNotExists(service);
                serviceRepository.save(service);
            }

            log.info("Services batch successfully imported, count={}", services.size());
        } catch (Exception e) {
            log.error("Failed to import services batch", e);
            throw new ServiceException("Failed to import services batch", e);
        }
    }

    @Override
    public void changeServicePrice(String serviceName, BigDecimal newPrice) {
        log.info("Changing service price: name='{}', newPrice={}", serviceName, newPrice);

        if (newPrice.compareTo(BigDecimal.ZERO) < 0) {
            log.warn("Invalid service price: {}", newPrice);
            throw new InvalidServicePriceException(newPrice);
        }

        try {
            HotelService service = getServiceByName(serviceName);
            service.setPrice(newPrice);

            serviceRepository.update(service);

            log.info("Service price successfully changed: name='{}', price={}", serviceName, newPrice);
        } catch (Exception e) {
            log.error("Failed to change service price: name='{}'", serviceName, e);
            throw new ServiceException("Failed to change service price", e);
        }
    }

    @Override
    public void exportServiceToCSV(String path) {
        log.info("Exporting services to CSV, path='{}'", path);

        if (path == null || path.isBlank()) {
            log.warn("CSV export path is empty");
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
            serviceRepository.findAll().forEach(s -> writer.println(s.toCsv()));

            log.info("Services successfully exported to CSV: {}", file.getAbsolutePath());
        } catch (IOException | RepositoryException e) {
            log.error("Failed to export services to CSV", e);
            throw new ServiceCsvException("Failed to export services: " + e.getMessage());
        }
    }

    @Override
    public void importServiceFromCSV(String path) {
        log.info("Importing services from CSV, path='{}'", path);

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine();
            List<HotelService> services = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                services.add(HotelService.fromCsv(line));
            }
            addServicesBatch(services);

            log.info("Services successfully imported from CSV, count={}", services.size());
        } catch (Exception e) {
            log.error("Failed to import services from CSV", e);
            throw new ServiceCsvException("Failed to import services: " + e.getMessage());
        }
    }

    private void ensureServiceNotExists(HotelService service) {
        try {
            serviceRepository.findByName(service.getName())
                    .ifPresent(s -> {
                        log.warn("Service already exists: {}", service.getName());
                        throw new ServiceAlreadyExistsException(service.getName());
                    });
        } catch (Exception e) {
            throw new ServiceException("Failed to validate service uniqueness", e);
        }
    }
}
