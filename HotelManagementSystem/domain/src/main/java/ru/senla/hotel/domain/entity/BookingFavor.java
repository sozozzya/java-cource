package ru.senla.hotel.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.Column;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "booking_services")
public class BookingFavor implements Serializable {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private Favor favor;

    @Getter
    @Column(nullable = false)
    private LocalDate date;

    public BookingFavor() {
    }

    public BookingFavor(Booking booking, Favor favor, LocalDate date) {
        this.booking = booking;
        this.favor = favor;
        this.date = date;
    }

    public BigDecimal getPrice() {
        return favor.getPrice();
    }

    @Override
    public String toString() {
        return "Favor: " + favor.getName() +
                " | Date: " + date +
                " | Price: " + favor.getPrice();
    }
}
