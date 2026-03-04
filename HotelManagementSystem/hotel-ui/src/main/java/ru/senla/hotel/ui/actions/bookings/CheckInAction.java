package ru.senla.hotel.ui.actions.bookings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.di.annotation.Scope;
import ru.senla.hotel.service.exception.booking.BookingException;
import ru.senla.hotel.service.exception.guest.GuestException;
import ru.senla.hotel.service.exception.room.RoomException;
import ru.senla.hotel.service.Administrator;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

import java.time.LocalDate;

@Component(scope = Scope.PROTOTYPE)
public class CheckInAction implements IAction {

    @Inject
    private Administrator admin;

    @Inject
    private ConsoleReader reader;

    private static final Logger log = LoggerFactory.getLogger(CheckInAction.class);

    @Override
    public void execute() {
        log.info("User command started: CHECK_IN");

        try {
            System.out.print("Enter guest name: ");
            String guestName = reader.nextLine();

            System.out.print("Enter room number: ");
            int roomNumber = reader.nextInt();

            System.out.print("Enter check-in date (YYYY-MM-DD): ");
            LocalDate checkIn = LocalDate.parse(reader.nextLine());

            System.out.print("Enter check-out date (YYYY-MM-DD): ");
            LocalDate checkOut = LocalDate.parse(reader.nextLine());

            admin.checkIn(guestName, roomNumber, checkIn, checkOut);
            System.out.println("Guest successfully checked in.");

            log.info("User command completed successfully: CHECK_IN");
        } catch (BookingException | RoomException | GuestException e) {
            log.error("User command failed: CHECK_IN", e);
            System.out.println("Check-in failed: " + e.getMessage());
        } catch (Exception e) {
            log.error("User command failed due to invalid input: CHECK_INY", e);
            System.out.println("Invalid input.");
        }
    }
}
