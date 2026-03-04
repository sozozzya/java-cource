package ru.senla.hotel.model;

import java.io.Serial;
import java.io.Serializable;

public class Guest implements Identifiable, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private final String name;

    public Guest(Long id, String name) {
        this.id = id;
        this.name = name;
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

    public String toCsv() {
        return id + ";" + name;
    }

    public static Guest fromCsv(String csv) {
        String[] p = csv.split(";", -1);
        return new Guest(Long.parseLong(p[0]), p[1]);
    }

    @Override
    public String toString() {
        return "Guest ID=" + id + ": " + name;
    }
}
