package ru.senla.hotel.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.Deque;

public class Room implements Identifiable, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private final int number;
    private final int capacity;
    private final int stars;
    private double pricePerNight;
    private boolean isUnderMaintenance;

    private final Deque<StayRecord> stayHistory = new ArrayDeque<>();

    public Room(Long id, int number, int capacity, int stars, double pricePerNight) {
        this.id = id;
        this.number = number;
        this.capacity = capacity;
        this.stars = stars;
        this.pricePerNight = pricePerNight;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getStars() {
        return stars;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public boolean isUnderMaintenance() {
        return isUnderMaintenance;
    }

    public Deque<StayRecord> getStayHistory() {
        return stayHistory;
    }

    public void addStayRecord(String guestName, LocalDate checkIn, LocalDate checkOut, int maxHistorySize) {
        stayHistory.addFirst(new StayRecord(guestName, checkIn, checkOut));

        while (stayHistory.size() > maxHistorySize) {
            stayHistory.removeLast();
        }
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public void setUnderMaintenance(boolean underMaintenance) {
        isUnderMaintenance = underMaintenance;
    }

    public String toCsv() {
        return id + ";" +
                number + ";" +
                capacity + ";" +
                stars + ";" +
                pricePerNight + ";" +
                isUnderMaintenance;
    }

    public static Room fromCsv(String csv) {
        String[] p = csv.split(";", -1);

        long id = Long.parseLong(p[0]);
        int number = Integer.parseInt(p[1]);
        int capacity = Integer.parseInt(p[2]);
        int stars = Integer.parseInt(p[3]);
        double price = Double.parseDouble(p[4]);
        boolean maintenance = Boolean.parseBoolean(p[5]);

        Room room = new Room(id, number, capacity, stars, price);
        room.isUnderMaintenance = maintenance;

        return room;
    }

    @Override
    public String toString() {
        return "Room " + number +
                " (ID=" + id + ")" +
                " | Capacity: " + capacity +
                " | " + stars + " ★" +
                " | Price: " + pricePerNight +
                " | Under maintenance: " + isUnderMaintenance;
    }
}
