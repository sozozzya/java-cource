package ru.senla.hotel.presentation.console.actions.guests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.senla.hotel.service.BookingServiceManager;
import ru.senla.hotel.service.GuestManager;
import ru.senla.hotel.service.exception.guest.GuestException;
import ru.senla.hotel.service.exception.service.ServiceException;
import ru.senla.hotel.presentation.console.actions.IAction;
import ru.senla.hotel.presentation.console.util.ConsoleReader;

import java.time.LocalDate;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AssignServiceToGuestAction implements IAction {

    private static final Logger log = LoggerFactory.getLogger(AssignServiceToGuestAction.class);

    private final BookingServiceManager bookingServiceManager;
    private final ConsoleReader reader;

    @Autowired
    public AssignServiceToGuestAction(BookingServiceManager bookingServiceManager,
                                      ConsoleReader reader) {
        this.bookingServiceManager = bookingServiceManager;
        this.reader = reader;
    }

    @Override
    public void execute() {
        log.info("User command started: ASSIGN_SERVICE_TO_GUEST");

        try {
            System.out.print("Enter guest name: ");
            String guestName = reader.nextLine();

            System.out.print("Enter service name: ");
            String serviceName = reader.nextLine();

            System.out.print("Enter service date (YYYY-MM-DD): ");
            LocalDate date = LocalDate.parse(reader.nextLine());

            bookingServiceManager.assignServiceToGuest(guestName, serviceName, date);

            log.info(
                    "User command completed successfully: ASSIGN_SERVICE_TO_GUEST, guest={}, service={}, date={}",
                    guestName, serviceName, date
            );
            System.out.println("Service successfully assigned to guest.");
        } catch (GuestException | ServiceException e) {
            log.error("User command failed: ASSIGN_SERVICE_TO_GUEST", e);
            System.out.println("Failed to assign service: " + e.getMessage());
        } catch (Exception e) {
            log.error("User command failed due to invalid input: ASSIGN_SERVICE_TO_GUEST", e);
            System.out.println("Invalid input.");
        }
    }
}
