package ru.senla.task4.hotel.model;

import java.time.LocalDate;

public class Service {
    private final String name;
    private double price;
    private final LocalDate date;

    public Service(String name, double price) {
        this(name, price, LocalDate.now());
    }

    public Service(String name, double price, LocalDate date) {
        this.name = name;
        this.price = price;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return name + " | Price: " + price + " | Date: " + date;
    }
}
