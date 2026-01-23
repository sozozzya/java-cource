package ru.senla.hotel.model;

import java.time.LocalDate;
import java.util.*;

public class Room {
    private final int number;
    private final int capacity;
    private final int stars;
    private double pricePerNight;
    private boolean isOccupied;
    private boolean isUnderMaintenance;
    private Guest currentGuest;
    private final Deque<StayRecord> stayHistory = new ArrayDeque<>();

    public Room(int number, int capacity, int stars, double pricePerNight) {
        this.number = number;
        this.capacity = capacity;
        this.stars = stars;
        this.pricePerNight = pricePerNight;
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

    public boolean isOccupied() {
        return isOccupied;
    }

    public boolean isUnderMaintenance() {
        return isUnderMaintenance;
    }

    public Guest getCurrentGuest() {
        return currentGuest;
    }

    public Deque<StayRecord> getStayHistory() {
        return stayHistory;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public void setUnderMaintenance(boolean underMaintenance) {
        isUnderMaintenance = underMaintenance;
    }

    public boolean occupy(Guest guest, LocalDate checkIn, LocalDate checkOut) {
        if (isUnderMaintenance || isOccupied) return false;

        this.isOccupied = true;
        this.currentGuest = guest;
        stayHistory.addFirst(new StayRecord(guest.getName(), checkIn, checkOut));
        if (stayHistory.size() > 3) stayHistory.removeLast();

        return true;
    }

    public boolean vacate() {
        if (!isOccupied) return false;

        this.currentGuest = null;
        this.isOccupied = false;
        return true;
    }

    @Override
    public String toString() {
        return "Room " + number + " | Capacity: " + capacity + " | " + stars + " ★ | Price: " + pricePerNight + " | Occupied: " + isOccupied + " | Under maintenance: " + isUnderMaintenance;
    }
}
