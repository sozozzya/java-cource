package ru.senla.task4.hotel.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public List<Service> getServices() {
        return services;
    }

    public void addService(Service service) {
        services.add(service);
    }

    public long getStayDuration() {
        long days = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        return (days <= 0) ? 1 : days;
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
        StringBuilder sb = new StringBuilder();
        sb.append("Booking Summary\n");
        sb.append("• Guest: ").append(guest.getName()).append("\n");
        sb.append("• Room: #").append(room.getNumber()).append(")\n");
        sb.append("• Check-in: ").append(checkInDate).append("\n");
        sb.append("• Check-out: ").append(checkOutDate).append("\n");

        if (!services.isEmpty()) {
            sb.append("• Services: ");
            for (int i = 0; i < services.size(); i++) {
                sb.append(services.get(i).getName());
                if (i < services.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append("\n");
        } else {
            sb.append("• Services: none\n");
        }

        sb.append(String.format("• Room cost: %.2f₽\n", calculateTotalRoomCost()));
        sb.append(String.format("• Services cost: %.2f₽\n", calculateTotalServicesCost()));
        sb.append(String.format("• Total cost: %.2f₽\n", calculateTotalCost()));

        return sb.toString();
    }
}
