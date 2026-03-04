package ru.senla.hotel.storage;

import ru.senla.hotel.model.*;

import java.io.Serializable;
import java.util.List;

public class AppState implements Serializable {
    private List<Room> rooms;
    private List<Guest> guests;
    private List<Service> services;
    private List<Booking> bookings;

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Guest> getGuests() {
        return guests;
    }

    public void setGuests(List<Guest> guests) {
        this.guests = guests;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
