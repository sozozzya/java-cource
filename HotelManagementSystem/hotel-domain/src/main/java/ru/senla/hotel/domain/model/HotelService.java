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
@Table(name = "services")
public class HotelService implements Identifiable, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    protected HotelService() {
    }

    public HotelService(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public HotelService(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String toCsv() {
        return id + ";" + name + ";" + price;
    }

    public static HotelService fromCsv(String csv) {
        String[] p = csv.split(";", -1);
        return new HotelService(
                Long.parseLong(p[0]),
                p[1],
                new BigDecimal(p[2])
        );
    }

    @Override
    public String toString() {
        return "Service ID=" + id +
                " | " + name +
                " | Price: " + price;
    }
}
