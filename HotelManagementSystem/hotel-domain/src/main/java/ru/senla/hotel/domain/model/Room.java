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
import java.math.BigDecimal;

@Entity
@Table(name = "rooms")
public class Room implements Identifiable, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private int number;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false)
    private int stars;

    @Column(name = "price_per_night", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerNight;

    @Column(name = "is_under_maintenance", nullable = false)
    private boolean isUnderMaintenance;

    protected Room() {
    }

    public Room(Long id, int number, int capacity, int stars, BigDecimal pricePerNight) {
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

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public boolean isUnderMaintenance() {
        return isUnderMaintenance;
    }

    public void setPricePerNight(BigDecimal pricePerNight) {
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
        BigDecimal price = new BigDecimal(p[4]);
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
