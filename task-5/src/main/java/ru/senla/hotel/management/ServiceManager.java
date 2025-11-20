package ru.senla.hotel.management;

import ru.senla.hotel.model.Service;

import java.util.*;

public class ServiceManager {
    private final List<Service> availableServices = new ArrayList<>();

    public void addService(Service service) {
        Service existing = findServiceByName(service.getName());
        if (existing != null) {
            System.out.println("Service " + service.getName() + " already exists.");
            return;
        }

        availableServices.add(service);
        System.out.println("Service " + service.getName() + " added.");
    }


    public Service findServiceByName(String name) {
        return availableServices.stream()
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public List<Service> getAvailableServices() {
        return Collections.unmodifiableList(availableServices);
    }
}
