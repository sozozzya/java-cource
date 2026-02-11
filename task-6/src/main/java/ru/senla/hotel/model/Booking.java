package ru.senla.hotel.model;

import ru.senla.hotel.exception.booking.InvalidBookingDatesException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Booking implements Identifiable {
    private Long id;

    private Guest guest;
    private Long guestId;

    private Room room;
    private Long roomId;

    private final LocalDate checkInDate;
    private LocalDate checkOutDate;

    private final List<Service> services = new ArrayList<>();
    private final List<Long> serviceIds = new ArrayList<>();

    public Booking(Guest guest, Room room,
                   LocalDate checkInDate, LocalDate checkOutDate) {
        this(null, guest, room, checkInDate, checkOutDate);
    }

    public Booking(Long id, Guest guest, Room room,
                   LocalDate checkInDate, LocalDate checkOutDate) {
        this.id = id;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;

        setGuest(guest);
        setRoom(room);
    }

    @Override
    public Long getId() {
        return id;
    }

    public Guest getGuest() {
        return guest;
    }

    public Long getGuestId() {
        return guestId;
    }

    public Room getRoom() {
        return room;
    }

    public Long getRoomId() {
        return roomId;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public List<Service> getServices() {
        return Collections.unmodifiableList(services);
    }

    public List<Long> getServiceIds() {
        return Collections.unmodifiableList(serviceIds);
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
        this.guestId = (guest != null ? guest.getId() : null);
    }

    public void setRoom(Room room) {
        this.room = room;
        this.roomId = (room != null ? room.getId() : null);
    }

    public void addService(Service service) {
        if (service == null) return;
        services.add(service);
        if (service.getId() != null) serviceIds.add(service.getId());
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

    public void forceCheckOut(LocalDate newCheckOutDate) {
        if (newCheckOutDate.isBefore(checkInDate)) {
            throw new InvalidBookingDatesException(checkInDate, newCheckOutDate);
        }
        this.checkOutDate = newCheckOutDate;
    }

    public String toCsv() {
        String servicesCsv = serviceIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        return id + ";" +
                guestId + ";" +
                roomId + ";" +
                checkInDate + ";" +
                checkOutDate + ";" +
                servicesCsv;
    }

    public static Booking fromCsv(String csv) {
        String[] p = csv.split(";", -1);

        Booking b = new Booking(
                Long.parseLong(p[0]),
                null,
                null,
                LocalDate.parse(p[3]),
                LocalDate.parse(p[4])
        );

        b.guestId = Long.parseLong(p[1]);
        b.roomId = Long.parseLong(p[2]);

        if (!p[5].isEmpty()) {
            for (String s : p[5].split(",")) {
                b.serviceIds.add(Long.parseLong(s));
            }
        }

        return b;
    }

    public void attachGuest(Guest guest) {
        this.guest = guest;
    }

    public void attachRoom(Room room) {
        this.room = room;
    }

    public void attachServices(List<Service> services) {
        this.services.addAll(services);
    }

    @Override
    public String toString() {
        String svcList = services.isEmpty() ? "none" :
                services.stream().map(Service::getName).collect(Collectors.joining(", "));

        String guestStr = guest != null ? guest.getName() : (", ID=" + guestId);
        String roomStr = room != null ? String.valueOf(room.getNumber()) : (", ID=" + roomId);

        return "Booking ID=" + id + "\n" +
                "• Guest: " + guestStr + "\n" +
                "• Room: " + roomStr + "\n" +
                "• Check-in: " + checkInDate + "\n" +
                "• Check-out: " + checkOutDate + "\n" +
                "• Services: " + svcList + "\n" +
                String.format("• Room cost: %.2f\n", calculateTotalRoomCost()) +
                String.format("• Services cost: %.2f\n", calculateTotalServicesCost()) +
                String.format("• Total cost: %.2f\n", calculateTotalCost());
    }
}
