package ru.senla.hotel.ui.actions.guests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.di.annotation.Scope;
import ru.senla.hotel.service.exception.guest.GuestException;
import ru.senla.hotel.service.exception.service.ServiceException;
import ru.senla.hotel.service.Administrator;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

import java.time.LocalDate;

@Component(scope = Scope.PROTOTYPE)
public class AssignServiceToGuestAction implements IAction {

    @Inject
    private Administrator admin;

    @Inject
    private ConsoleReader reader;

    private static final Logger log = LoggerFactory.getLogger(AssignServiceToGuestAction.class);

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

            admin.assignServiceToGuest(guestName, serviceName, date);

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
