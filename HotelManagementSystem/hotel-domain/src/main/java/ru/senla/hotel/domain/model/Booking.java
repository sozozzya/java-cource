package ru.senla.hotel.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import ru.senla.hotel.domain.base.Identifiable;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "bookings")
public class Booking implements Identifiable, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "guest_id", nullable = false)
    private Guest guest;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingService> services = new ArrayList<>();

    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;

    @Column(name = "check_out_date", nullable = false)
    private LocalDate checkOutDate;

    protected Booking() {
    }

    public Booking(Guest guest, Room room,
                   LocalDate checkInDate, LocalDate checkOutDate) {
        this.guest = guest;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    @Override
    public Long getId() {
        return id;
    }

    public Guest getGuest() {
        return guest;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public List<BookingService> getServices() {
        return Collections.unmodifiableList(services);
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void addService(HotelService service, LocalDate date) {
        if (service == null) return;
        services.add(new BookingService(this, service, date));
    }

    public long getStayDuration() {
        long days = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        return Math.max(days, 1);
    }

    public BigDecimal calculateTotalRoomCost() {
        return room.getPricePerNight().multiply(BigDecimal.valueOf(getStayDuration()));
    }

    public BigDecimal calculateTotalServicesCost() {
        return services.stream()
                .map(BookingService::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateTotalCost() {
        return calculateTotalRoomCost().add(calculateTotalServicesCost());
    }

    public void forceCheckOut(LocalDate newCheckOutDate) {
        this.checkOutDate = newCheckOutDate;
    }

    @Override
    public String toString() {
        String svcList = services.isEmpty() ? "none" :
                services.stream()
                        .map(bs -> bs.getService().getName() +
                                " (" + bs.getDate() + ")")
                        .collect(Collectors.joining(", "));

        String guestStr = guest != null ? guest.getName() : ("none");
        String roomStr = room != null ? String.valueOf(room.getNumber()) : ("none");

        return "Booking ID=" + id + "\n" +
                "• Guest: " + guestStr + "\n" +
                "• Room: " + roomStr + "\n" +
                "• Check-in: " + checkInDate + "\n" +
                "• Check-out: " + checkOutDate + "\n" +
                "• Services: " + svcList + "\n" +
                String.format("• Room cost: %.2f\n", calculateTotalRoomCost()) +
                String.format("• Services cost: %.2f\n", calculateTotalServicesCost()) +
                String.format("• Total cost: %.2f\n", calculateTotalCost());
    }
}
