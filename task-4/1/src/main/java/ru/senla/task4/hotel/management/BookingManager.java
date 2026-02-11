package ru.senla.task4.hotel.management;

import ru.senla.task4.hotel.model.Booking;
import ru.senla.task4.hotel.model.Guest;
import ru.senla.task4.hotel.model.Room;
import ru.senla.task4.hotel.model.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class BookingManager {
    private final RoomManager roomManager;
    private final ServiceManager serviceManager;
    private final List<Booking> bookings = new ArrayList<>();

    public BookingManager(RoomManager roomManager, ServiceManager serviceManager) {
        this.roomManager = roomManager;
        this.serviceManager = serviceManager;
    }

    public void checkIn(Guest guest, int roomNumber, LocalDate checkIn, LocalDate checkOut) {
        Room room = roomManager.findRoom(roomNumber);
        if (room == null) {
            System.out.println("Room " + roomNumber + " not found.");
            return;
        }

        if (room.isUnderMaintenance()) {
            System.out.println("Cannot check in. Room " + roomNumber + " is under maintenance.");
            return;
        }
        if (room.isOccupied()) {
            System.out.println("Cannot check in. Room " + roomNumber + " is already occupied.");
            return;
        }
        if (checkOut.isBefore(checkIn)) {
            System.out.println("Invalid dates: check-out (" + checkOut + ") cannot be before check-in (" + checkIn + ").");
            return;
        }

        if (room.occupy(guest, checkIn, checkOut)) {
            guest.setCheckInDate(checkIn);
            guest.setCheckOutDate(checkOut);
            bookings.add(new Booking(guest, room, checkIn, checkOut));
            System.out.println(guest.getName() +
                    " checked into room " + roomNumber +
                    " " + checkIn +
                    " and will check out " + checkOut + ".");
        } else {
            System.out.println("Check-in failed for room " + roomNumber + ".");
        }
    }

    public void checkOut(int roomNumber) {
        Room room = roomManager.findRoom(roomNumber);
        if (room == null) {
            System.out.println("Room " + roomNumber + " not found.");
            return;
        }

        if (!room.isOccupied()) {
            System.out.println("Room " + roomNumber + " is already vacant.");
            return;
        }

        Guest guest = room.getCurrentGuest();

        if (room.vacate()) {
            System.out.println(guest.getName() + " checked out from room " + roomNumber + ".");
        } else {
            System.out.println("Failed to check out from room " + roomNumber + ".");
        }
    }

    public void assignServiceToGuest(Guest guest, String serviceName, LocalDate date) {
        Service baseService = serviceManager.findServiceByName(serviceName);
        if (baseService == null) {
            System.out.println("Service " + serviceName + " not found.");
            return;
        }

        Service datedService = new Service(baseService.getName(), baseService.getPrice(), date);

        Booking activeBooking = findActiveBookingForGuest(guest);
        if (activeBooking != null) {
            activeBooking.addService(datedService);
            System.out.println(guest.getName() + " has booked "
                    + serviceName + " (" + baseService.getPrice() + "₽) on " + date + ".");
        } else {
            System.out.println("No active booking found for " + guest.getName() + ".");
        }
    }

    public List<Booking> getAllBookings() {
        return Collections.unmodifiableList(bookings);
    }

    public List<Guest> getCurrentGuests() {
        return bookings.stream().map(Booking::getGuest).distinct().toList();
    }

    public List<Booking> getActiveBookings() {
        LocalDate today = LocalDate.now();
        return bookings.stream()
                .filter(b -> !b.getCheckOutDate().isBefore(today))
                .collect(Collectors.toList());
    }

    public long getCurrentGuestsCount() {
        return bookings.stream().map(Booking::getGuest).distinct().count();
    }

    public List<Room> getRoomsFreeByDate(LocalDate date) {
        return roomManager.getAllRooms().stream()
                .filter(r -> bookings.stream()
                        .noneMatch(b -> b.getRoom().equals(r)
                                && !b.getCheckOutDate().isBefore(date)))
                .collect(Collectors.toList());
    }

    public double calculateGuestRoomCost(Guest guest) {
        return bookings.stream()
                .filter(b -> b.getGuest().equals(guest))
                .mapToDouble(Booking::calculateTotalRoomCost)
                .sum();
    }

    public double calculateGuestServicesCost(Guest guest) {
        return bookings.stream()
                .filter(b -> b.getGuest().equals(guest))
                .mapToDouble(Booking::calculateTotalServicesCost)
                .sum();
    }

    public double calculateGuestTotalCost(Guest guest) {
        return calculateGuestRoomCost(guest) + calculateGuestServicesCost(guest);
    }

    public List<Service> getServicesByGuest(Guest guest) {
        return bookings.stream()
                .filter(b -> b.getGuest().equals(guest))
                .flatMap(b -> b.getServices().stream())
                .collect(Collectors.toList());
    }

    private Booking findActiveBookingForGuest(Guest guest) {
        return getActiveBookings().stream()
                .filter(b -> b.getGuest().equals(guest))
                .findFirst()
                .orElse(null);
    }
}
