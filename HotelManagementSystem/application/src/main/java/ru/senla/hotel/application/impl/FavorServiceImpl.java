package ru.senla.hotel.application.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.senla.hotel.application.util.sorting.ComparatorRegistry;
import ru.senla.hotel.application.util.sorting.SortingService;
import ru.senla.hotel.domain.entity.Favor;
import ru.senla.hotel.domain.enums.SortField;
import ru.senla.hotel.infrastructure.exception.RepositoryException;
import ru.senla.hotel.application.service.FavorService;
import ru.senla.hotel.application.exception.favor.FavorCsvException;
import ru.senla.hotel.application.exception.favor.InvalidFavorPriceException;
import ru.senla.hotel.application.exception.favor.FavorAlreadyExistsException;
import ru.senla.hotel.application.exception.favor.FavorNotFoundException;
import ru.senla.hotel.application.exception.favor.FavorException;
import ru.senla.hotel.infrastructure.repository.FavorRepository;

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
public class FavorServiceImpl implements FavorService {

    private static final Logger log = LoggerFactory.getLogger(FavorServiceImpl.class);

    private final FavorRepository favorRepository;
    private final SortingService sortingService;
    private final ComparatorRegistry comparatorRegistry;

    public FavorServiceImpl(FavorRepository favorRepository,
                            SortingService sortingService,
                            ComparatorRegistry comparatorRegistry) {
        this.favorRepository = favorRepository;
        this.sortingService = sortingService;
        this.comparatorRegistry = comparatorRegistry;
    }

    public Optional<Favor> findById(Long id) {
        log.debug("Finding favor by id: {}", id);
        try {
            return Optional.ofNullable(favorRepository.findById(id));
        } catch (Exception e) {
            log.error("Failed to find favor by id: {}", id, e);
            throw new FavorException("Failed to find favor", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Favor> findFavorByName(String name) {
        log.debug("Finding favor by name: {}", name);
        try {
            return favorRepository.findByName(name);
        } catch (Exception e) {
            log.error("Failed to find service by name: {}", name, e);
            throw new FavorException("Failed to find service by name", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Favor getFavorByName(String name) {
        log.debug("Getting service by name: {}", name);

        return findFavorByName(name)
                .orElseThrow(() -> {
                    log.warn("Service not found: {}", name);
                    return new FavorNotFoundException(name);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Favor> getAvailableFavors(SortField field) {
        log.debug("Fetching all available services");

        try {
            List<Favor> favors = favorRepository.findAll();

            return sortingService.sort(
                    favors,
                    comparatorRegistry.favorComparator(field)
            );
        } catch (Exception e) {
            log.error("Failed to fetch services", e);
            throw new FavorException("Failed to fetch services", e);
        }
    }

    @Override
    public Favor addFavor(Favor favor) {
        log.info("Adding service: name={}, price={}", favor.getName(), favor.getPrice());

        try {
            ensureFavorNotExists(favor);

            Favor saved = favorRepository.save(favor);

            log.info("Service successfully added: id={}, name='{}'", saved.getId(), saved.getName());
            return saved;
        } catch (Exception e) {
            log.error("Failed to add service with name='{}'", favor.getName(), e);
            throw new FavorException("Failed to add service", e);
        }
    }

    @Override
    public void addFavorsBatch(List<Favor> favors) {
        log.info("Importing services batch, size={}", favors.size());

        try {
            for (Favor service : favors) {
                ensureFavorNotExists(service);
                favorRepository.save(service);
            }

            log.info("Services batch successfully imported, count={}", favors.size());
        } catch (Exception e) {
            log.error("Failed to import services batch", e);
            throw new FavorException("Failed to import services batch", e);
        }
    }

    @Override
    public void changeFavorPrice(String serviceName, BigDecimal newPrice) {
        log.info("Changing service price: name='{}', newPrice={}", serviceName, newPrice);

        if (newPrice.compareTo(BigDecimal.ZERO) < 0) {
            log.warn("Invalid service price: {}", newPrice);
            throw new InvalidFavorPriceException(newPrice);
        }

        try {
            Favor service = getFavorByName(serviceName);
            service.setPrice(newPrice);

            favorRepository.update(service);

            log.info("Service price successfully changed: name='{}', price={}", serviceName, newPrice);
        } catch (Exception e) {
            log.error("Failed to change service price: name='{}'", serviceName, e);
            throw new FavorException("Failed to change service price", e);
        }
    }

    @Override
    public void exportFavorToCSV(String path) {
        log.info("Exporting services to CSV, path='{}'", path);

        if (path == null || path.isBlank()) {
            log.warn("CSV export path is empty");
            throw new FavorCsvException("CSV export path cannot be empty.");
        }

        File file = new File(path);

        if (file.isDirectory()) {
            file = new File(file, "services.csv");
        } else {
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                throw new FavorCsvException(
                        "Invalid file format. CSV file expected: " + file.getName()
                );
            }
        }

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("id;name;price;date");
            favorRepository.findAll().forEach(s -> writer.println(s.toCsv()));

            log.info("Services successfully exported to CSV: {}", file.getAbsolutePath());
        } catch (IOException | RepositoryException e) {
            log.error("Failed to export services to CSV", e);
            throw new FavorCsvException("Failed to export services: " + e.getMessage());
        }
    }

    @Override
    public void importFavorFromCSV(String path) {
        log.info("Importing services from CSV, path='{}'", path);

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine();
            List<Favor> services = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                services.add(Favor.fromCsv(line));
            }
            addFavorsBatch(services);

            log.info("Services successfully imported from CSV, count={}", services.size());
        } catch (Exception e) {
            log.error("Failed to import services from CSV", e);
            throw new FavorCsvException("Failed to import services: " + e.getMessage());
        }
    }

    private void ensureFavorNotExists(Favor service) {
        try {
            favorRepository.findByName(service.getName())
                    .ifPresent(s -> {
                        log.warn("Service already exists: {}", service.getName());
                        throw new FavorAlreadyExistsException(service.getName());
                    });
        } catch (Exception e) {
            throw new FavorException("Failed to validate service uniqueness", e);
        }
    }
}
