package ru.senla.hotel.ui.actions.rooms;

import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.di.annotation.Scope;
import ru.senla.hotel.exception.HotelException;
import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

@Component(scope = Scope.PROTOTYPE)
public class ShowRoomDetailsAction implements IAction {

    @Inject
    private Administrator admin;

    @Inject
    private ConsoleReader reader;

    @Override
    public void execute() {
        try {
            System.out.print("Enter room number: ");
            int number = reader.nextInt();

            admin.printRoomDetails(number);

        } catch (HotelException e) {
            System.out.println("Failed to show room details: " + e.getMessage());
        }
    }
}
