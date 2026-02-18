package ru.senla.hotel.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.Column;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "booking_services")
public class BookingService implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private HotelService service;

    @Column(nullable = false)
    private LocalDate date;

    protected BookingService() {
    }

    public BookingService(Booking booking, HotelService service, LocalDate date) {
        this.booking = booking;
        this.service = service;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public HotelService getService() {
        return service;
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getPrice() {
        return service.getPrice();
    }

    @Override
    public String toString() {
        return "Service: " + service.getName() +
                " | Date: " + date +
                " | Price: " + service.getPrice();
    }
}
