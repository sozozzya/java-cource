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

import java.util.List;

public class ConsoleMenuFactory implements AbstractMenuFactory {

    private final Administrator admin;

    public ConsoleMenuFactory(Administrator admin) {
        this.admin = admin;
    }

    @Override
    public Menu createRootMenu() {

        Menu sortAvailableRoomsMenu = new Menu("Sort available rooms", List.of(
                new MenuItem("By price", new ShowAvailableRoomsAction(admin, "price"), null),
                new MenuItem("By capacity", new ShowAvailableRoomsAction(admin, "capacity"), null),
                new MenuItem("By stars", new ShowAvailableRoomsAction(admin, "stars"), null),
                new MenuItem("Back", new BackAction())
        ));

        Menu bookingsMenu = new Menu(MenuTitles.BOOKINGS.get(), List.of(
                new MenuItem("Check in guest", new CheckInAction(admin), null),
                new MenuItem("Check out (by room)", new CheckOutAction(admin), null),
                new MenuItem("Show available rooms", new NavigateAction(sortAvailableRoomsMenu), sortAvailableRoomsMenu),
                new MenuItem("Count available rooms", new ShowAvailableRoomCountAction(admin), null),
                new MenuItem("Rooms free by date", new ShowRoomsFreeByDateAction(admin), null),
                new MenuItem("Back to main", new BackAction())
        ));

        Menu sortAllRoomsMenu = new Menu("Sort all rooms", List.of(
                new MenuItem("By price", new ShowAllRoomsAction(admin, "price"), null),
                new MenuItem("By capacity", new ShowAllRoomsAction(admin, "capacity"), null),
                new MenuItem("By stars", new ShowAllRoomsAction(admin, "stars"), null),
                new MenuItem("Back", new BackAction())
        ));

        Menu roomsMenu = new Menu(MenuTitles.ROOMS.get(), List.of(
                new MenuItem("Add room", new AddRoomAction(admin), null),
                new MenuItem("Set room maintenance", new SetMaintenanceAction(admin), null),
                new MenuItem("Change room price", new ChangeRoomPriceAction(admin), null),
                new MenuItem("Show room details", new ShowRoomDetailsAction(admin), null),
                new MenuItem("Show room history", new ShowRoomHistoryAction(admin), null),
                new MenuItem("Show all rooms", new NavigateAction(sortAllRoomsMenu), sortAllRoomsMenu),
                new MenuItem("Back to main", new BackAction())
        ));

        Menu servicesMenu = new Menu(MenuTitles.SERVICES.get(), List.of(
                new MenuItem("Add service", new AddServiceAction(admin), null),
                new MenuItem("Change service price", new ChangeServicePriceAction(admin), null),
                new MenuItem("Show all services", new ShowAllServicesAction(admin), null),
                new MenuItem("Back to main", new BackAction())
        ));

        Menu sortGuestsMenu = new Menu("Sort guests", List.of(
                new MenuItem("By name", new ShowGuestsAction(admin, "name"), null),
                new MenuItem("By checkout date", new ShowGuestsAction(admin, "checkout"), null),
                new MenuItem("Back", new BackAction())
        ));

        Menu sortGuestServicesMenu = new Menu("Sort guest services", List.of(
                new MenuItem("By price", new ShowGuestServicesAction(admin, "price"), null),
                new MenuItem("By date", new ShowGuestServicesAction(admin, "date"), null),
                new MenuItem("Back", new BackAction())
        ));

        Menu guestsMenu = new Menu(MenuTitles.GUESTS.get(), List.of(
                new MenuItem("Assign service to guest", new AssignServiceToGuestAction(admin), null),
                new MenuItem("Show guests", new NavigateAction(sortGuestsMenu), sortGuestsMenu),
                new MenuItem("Count current guests", new ShowGuestCountAction(admin), null),
                new MenuItem("Show guest bill", new ShowGuestBillAction(admin), null),
                new MenuItem("Show guest services", new NavigateAction(sortGuestServicesMenu), sortGuestServicesMenu),
                new MenuItem("Back to main", new BackAction())
        ));

        Menu reportsMenu = new Menu(MenuTitles.REPORTS.get(), List.of(
                new MenuItem("Show prices overview", new ShowAllPricesAction(admin), null),
                new MenuItem("Back to main", new BackAction())
        ));

        return new Menu(MenuTitles.MAIN.get(), List.of(
                new MenuItem("Bookings", new NavigateAction(bookingsMenu), bookingsMenu),
                new MenuItem("Rooms", new NavigateAction(roomsMenu), roomsMenu),
                new MenuItem("Services", new NavigateAction(servicesMenu), servicesMenu),
                new MenuItem("Guests", new NavigateAction(guestsMenu), guestsMenu),
                new MenuItem("Reports", new NavigateAction(reportsMenu), reportsMenu),
                new MenuItem("Exit", new ExitAction(), null)
        ));
    }
}