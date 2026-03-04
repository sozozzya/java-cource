package ru.senla.hotel.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import ru.senla.hotel.domain.base.Identifiable;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "guests")
public class Guest implements Identifiable, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    protected Guest() {
    }

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

    public void setName(String name) {
        this.name = name;
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
