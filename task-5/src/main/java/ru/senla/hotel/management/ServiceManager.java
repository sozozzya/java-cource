package ru.senla.hotel.management;

import ru.senla.hotel.model.Service;

import java.util.*;

public class ServiceManager {
    private final List<Service> availableServices = new ArrayList<>();

    public void addService(Service service) {
        boolean exists = availableServices.stream()
                .anyMatch(s -> s.getName().equalsIgnoreCase(service.getName()));

        if (exists) {
            System.out.println("Service " + service.getName() + " already exists.");
            return;
        }

        availableServices.add(service);
        System.out.println("Service " + service.getName() + " added.");
    }

    public Optional<Service> findServiceOptional(String name) {
        return availableServices.stream()
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public List<Service> getAvailableServices() {
        return Collections.unmodifiableList(availableServices);
    }
}
