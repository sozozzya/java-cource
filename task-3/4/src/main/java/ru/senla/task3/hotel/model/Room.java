package ru.senla.task3.hotel.model;

public class Room {
    private final int number;
    private double pricePerNight;
    private boolean isOccupied;
    private boolean isUnderMaintenance;

    public Room(int number, double pricePerNight) {
        this.number = number;
        this.pricePerNight = pricePerNight;
        this.isOccupied = false;
        this.isUnderMaintenance = false;
    }

    public int getNumber() {
        return number;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public boolean isUnderMaintenance() {
        return isUnderMaintenance;
    }

    public void setUnderMaintenance(boolean underMaintenance) {
        isUnderMaintenance = underMaintenance;
    }

    public void occupy() {
        if (isUnderMaintenance) {
            System.out.println("Cannot check in to room " + number + ". The room is currently under maintenance.\n");
        } else if (isOccupied) {
            System.out.println("Cannot check in to room " + number + ". The room is already occupied.\n");
        } else {
            isOccupied = true;
            System.out.println("Guest has been checked into room " + number + ".\n");
        }
    }

    public void vacate() {
        if (isOccupied) {
            isOccupied = false;
            System.out.println("Guest has been checked out from room " + number + ".\n");
        } else {
            System.out.println("Room " + number + " is already vacant.\n");
        }
    }

    @Override
    public String toString() {
        return "Room " + number +
                " | Price: " + pricePerNight +
                " | Occupied: " + isOccupied +
                " | Under maintenance: " + isUnderMaintenance;
    }
}
