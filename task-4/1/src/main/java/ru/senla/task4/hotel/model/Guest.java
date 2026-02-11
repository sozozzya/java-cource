package ru.senla.task4.hotel.model;

import java.time.LocalDate;

public class Guest {
    private final String name;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    public Guest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    @Override
    public String toString() {
        if (checkInDate == null || checkOutDate == null) {
            return name + " | Not currently checked in";
        } else {
            return name + " | Check-in: " + checkInDate + " | Check-out: " + checkOutDate;
        }
    }
}
