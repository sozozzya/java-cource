package ru.senla.hotel.presentation.console.actions.bookings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.senla.hotel.service.BookingManager;
import ru.senla.hotel.service.exception.booking.BookingException;
import ru.senla.hotel.service.exception.guest.GuestException;
import ru.senla.hotel.service.exception.room.RoomException;
import ru.senla.hotel.presentation.console.actions.IAction;
import ru.senla.hotel.presentation.console.util.ConsoleReader;

import java.time.LocalDate;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CheckInAction implements IAction {

    private static final Logger log = LoggerFactory.getLogger(CheckInAction.class);

    private final BookingManager bookingManager;
    private final ConsoleReader consoleReader;

    @Autowired
    public CheckInAction(BookingManager bookingManager,
                         ConsoleReader consoleReader) {
        this.bookingManager = bookingManager;
        this.consoleReader = consoleReader;
    }

    @Override
    public void execute() {
        log.info("User command started: CHECK_IN");

        try {
            System.out.print("Enter guest name: ");
            String guestName = consoleReader.nextLine();

            System.out.print("Enter room number: ");
            int roomNumber = consoleReader.nextInt();

            System.out.print("Enter check-in date (YYYY-MM-DD): ");
            LocalDate checkIn = LocalDate.parse(consoleReader.nextLine());

            System.out.print("Enter check-out date (YYYY-MM-DD): ");
            LocalDate checkOut = LocalDate.parse(consoleReader.nextLine());

            bookingManager.checkIn(guestName, roomNumber, checkIn, checkOut);
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
