package ru.senla.hotel.ui.actions.guests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.di.annotation.Scope;
import ru.senla.hotel.service.Administrator;
import ru.senla.hotel.service.exception.guest.GuestException;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

@Component(scope = Scope.PROTOTYPE)
public class ShowGuestBillAction implements IAction {

    @Inject
    private Administrator admin;

    @Inject
    private ConsoleReader reader;

    private static final Logger log = LoggerFactory.getLogger(ShowGuestBillAction.class);

    @Override
    public void execute() {
        log.info("User command started: SHOW_GUEST_BILL");

        try {
            System.out.print("Enter guest name: ");
            String guestName = reader.nextLine();

            admin.printGuestBill(guestName);

            log.info("User command completed successfully: SHOW_GUEST_BILL, guest={}", guestName);
        } catch (GuestException e) {
            log.error("User command failed: SHOW_GUEST_BILL", e);
            System.out.println("Failed to show guest bill: " + e.getMessage());
        }
    }
}
