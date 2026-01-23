package ru.senla.task4.hotel.management;

import ru.senla.task4.hotel.model.Service;

import java.util.*;

public class ServiceManager {
    private final List<Service> availableServices = new ArrayList<>();

    public void addService(Service service) {
        availableServices.add(service);
        System.out.println("Service " + service + " added.");
    }

    public void removeService(String serviceName) {
        availableServices.removeIf(s -> s.getName().equalsIgnoreCase(serviceName));
        System.out.println("Service " + serviceName + " removed.");
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
