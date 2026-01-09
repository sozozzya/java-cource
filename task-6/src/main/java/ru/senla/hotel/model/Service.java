package ru.senla.hotel.model;

import java.time.LocalDate;

public class Service implements Identifiable {
    private Long id;

    private final String name;
    private double price;
    private final LocalDate date;

    public Service(String name, double price, LocalDate date) {
        this.name = name;
        this.price = price;
        this.date = date;
    }

    public Service(Long id, String name, double price, LocalDate date) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.date = date;
    }

    public Service(String name, double price) {
        this(null, name, price, LocalDate.now());
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
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

    public String toCsv() {
        return id + ";" + name + ";" + price + ";" + date;
    }

    public static Service fromCsv(String csv) {
        String[] p = csv.split(";", -1);
        return new Service(
                Long.parseLong(p[0]),
                p[1],
                Double.parseDouble(p[2]),
                LocalDate.parse(p[3])
        );
    }

    @Override
    public String toString() {
        return "Service ID=" + id + ": " + name +
                " | Price: " + price +
                " | Date: " + date;
    }
}
