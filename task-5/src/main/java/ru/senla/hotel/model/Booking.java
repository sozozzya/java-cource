package ru.senla.hotel.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Booking {
    private final Guest guest;
    private final Room room;
    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;
    private final List<Service> services = new ArrayList<>();

    public Booking(Guest guest, Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        this.guest = guest;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public Guest getGuest() {
        return guest;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public List<Service> getServices() {
        return Collections.unmodifiableList(services);
    }

    public void addService(Service service) {
        services.add(service);
    }

    public long getStayDuration() {
        long days = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        return Math.max(days, 1);
    }

    public double calculateTotalRoomCost() {
        return room.getPricePerNight() * getStayDuration();
    }

    public double calculateTotalServicesCost() {
        return services.stream().mapToDouble(Service::getPrice).sum();
    }

    public double calculateTotalCost() {
        return calculateTotalRoomCost() + calculateTotalServicesCost();
    }

    @Override
    public String toString() {
        String serviceList = services.isEmpty()
                ? "none"
                : services.stream()
                .map(Service::getName)
                .collect(Collectors.joining(", "));

        return "Booking Summary\n" +
                "• Guest: " + guest.getName() + "\n" +
                "• Room: #" + room.getNumber() + ")\n" +
                "• Check-in: " + checkInDate + "\n" +
                "• Check-out: " + checkOutDate + "\n" +
                "• Services: " + serviceList + "\n" +
                String.format("• Room cost: %.2f₽\n", calculateTotalRoomCost()) +
                String.format("• Services cost: %.2f₽\n", calculateTotalServicesCost()) +
                String.format("• Total cost: %.2f₽\n", calculateTotalCost());
    }
}
