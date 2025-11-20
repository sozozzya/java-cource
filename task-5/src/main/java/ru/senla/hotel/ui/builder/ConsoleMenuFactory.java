package ru.senla.hotel.ui.builder;

import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.actions.*;
import ru.senla.hotel.ui.actions.bookings.*;
import ru.senla.hotel.ui.actions.rooms.*;
import ru.senla.hotel.ui.actions.reports.*;
import ru.senla.hotel.ui.actions.services.*;
import ru.senla.hotel.ui.actions.guests.*;
import ru.senla.hotel.ui.menu.Menu;
import ru.senla.hotel.ui.menu.MenuItem;
import ru.senla.hotel.ui.util.MenuTitles;

public class ConsoleMenuFactory implements AbstractMenuFactory {

    private final Administrator admin;

    public ConsoleMenuFactory(Administrator admin) {
        this.admin = admin;
    }

    @Override
    public Menu createRootMenu() {

        Menu bookingsMenu = new Menu(MenuTitles.BOOKINGS.get(), new MenuItem[]{
                new MenuItem("Check in guest", new CheckInAction(admin), null),
                new MenuItem("Check out (by room)", new CheckOutAction(admin), null),
                new MenuItem("Show available rooms (by price)", new ShowAvailableRoomsAction(admin, "price"), null),
                new MenuItem("Show available rooms (by capacity)", new ShowAvailableRoomsAction(admin, "capacity"), null),
                new MenuItem("Show available rooms (by stars)", new ShowAvailableRoomsAction(admin, "stars"), null),
                new MenuItem("Count available rooms", new ShowAvailableRoomCountAction(admin), null),
                new MenuItem("Rooms free by date", new ShowRoomsFreeByDateAction(admin), null),
                new MenuItem("Back to main", new BackAction())
        });

        Menu roomsMenu = new Menu(MenuTitles.ROOMS.get(), new MenuItem[]{
                new MenuItem("Add room", new AddRoomAction(admin), null),
                new MenuItem("Set room maintenance", new SetMaintenanceAction(admin), null),
                new MenuItem("Change room price", new ChangeRoomPriceAction(admin), null),
                new MenuItem("Show room details", new ShowRoomDetailsAction(admin), null),
                new MenuItem("Show room history (last 3)", new ShowRoomHistoryAction(admin), null),
                new MenuItem("Show all rooms (by price)", new ShowAllRoomsAction(admin, "price"), null),
                new MenuItem("Show all rooms (by capacity)", new ShowAllRoomsAction(admin, "capacity"), null),
                new MenuItem("Show all rooms (by stars)", new ShowAllRoomsAction(admin, "stars"), null),
                new MenuItem("Back to main", new BackAction())
        });

        Menu servicesMenu = new Menu(MenuTitles.SERVICES.get(), new MenuItem[]{
                new MenuItem("Add service", new AddServiceAction(admin), null),
                new MenuItem("Change service price", new ChangeServicePriceAction(admin), null),
                new MenuItem("Show all services", new ShowAllServicesAction(admin), null),
                new MenuItem("Back to main", new BackAction())
        });

        Menu guestsMenu = new Menu(MenuTitles.GUESTS.get(), new MenuItem[]{
                new MenuItem("Assign service to guest", new AssignServiceToGuestAction(admin), null),
                new MenuItem("Show guests (by name)", new ShowGuestsAction(admin, "name"), null),
                new MenuItem("Show guests (by checkout)", new ShowGuestsAction(admin, "checkout"), null),
                new MenuItem("Count current guests", new ShowGuestCountAction(admin), null),
                new MenuItem("Show guest bill", new ShowGuestBillAction(admin), null),
                new MenuItem("Show guest services (by price)", new ShowGuestServicesAction(admin, "price"), null),
                new MenuItem("Show guest services (by date)", new ShowGuestServicesAction(admin, "date"), null),
                new MenuItem("Back to main", new BackAction())
        });

        Menu reportsMenu = new Menu(MenuTitles.REPORTS.get(), new MenuItem[]{
                new MenuItem("Show prices overview", new ShowAllPricesAction(admin), null),
                new MenuItem("Back to main", new BackAction())
        });

        return new Menu(MenuTitles.MAIN.get(), new MenuItem[]{
                new MenuItem("Bookings", new NavigateAction(bookingsMenu), bookingsMenu),
                new MenuItem("Rooms", new NavigateAction(roomsMenu), roomsMenu),
                new MenuItem("Services", new NavigateAction(servicesMenu), servicesMenu),
                new MenuItem("Guests", new NavigateAction(guestsMenu), guestsMenu),
                new MenuItem("Reports", new NavigateAction(reportsMenu), reportsMenu),
                new MenuItem("Exit", new ExitAction(), null)
        });
    }
}