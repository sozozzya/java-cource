package ru.senla.hotel.presentation.console.actions.guests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.senla.hotel.presentation.console.ConsoleReportPrinter;
import ru.senla.hotel.service.exception.guest.GuestException;
import ru.senla.hotel.presentation.console.actions.IAction;
import ru.senla.hotel.presentation.console.util.ConsoleReader;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ShowGuestBillAction implements IAction {

    private static final Logger log = LoggerFactory.getLogger(ShowGuestBillAction.class);

    private final ConsoleReportPrinter consoleReportPrinter;
    private final ConsoleReader reader;

    @Autowired
    public ShowGuestBillAction(ConsoleReportPrinter consoleReportPrinter,
                               ConsoleReader reader) {
        this.consoleReportPrinter = consoleReportPrinter;
        this.reader = reader;
    }

    @Override
    public void execute() {
        log.info("User command started: SHOW_GUEST_BILL");

        try {
            System.out.print("Enter guest name: ");
            String guestName = reader.nextLine();

            consoleReportPrinter.printGuestBill(guestName);

            log.info("User command completed successfully: SHOW_GUEST_BILL, guest={}", guestName);
        } catch (GuestException e) {
            log.error("User command failed: SHOW_GUEST_BILL", e);
            System.out.println("Failed to show guest bill: " + e.getMessage());
        }
    }
}
