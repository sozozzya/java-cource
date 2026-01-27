package ru.senla.hotel.ui.builder;

import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.ui.actions.*;
import ru.senla.hotel.ui.actions.bookings.*;
import ru.senla.hotel.ui.actions.rooms.*;
import ru.senla.hotel.ui.actions.reports.*;
import ru.senla.hotel.ui.actions.services.*;
import ru.senla.hotel.ui.actions.guests.*;
import ru.senla.hotel.ui.menu.Menu;
import ru.senla.hotel.ui.menu.MenuItem;
import ru.senla.hotel.ui.util.ActionFactory;
import ru.senla.hotel.ui.util.MenuTitles;

import java.util.List;

@Component
public class ConsoleMenuFactory implements AbstractMenuFactory {

    @Inject
    private ActionFactory actionFactory;

    @Override
    public Menu createRootMenu() {

        Menu sortAvailableRoomsMenu = new Menu("Sort available rooms", List.of(
                new MenuItem("By price", actionFactory.inject(new ShowAvailableRoomsAction("price")), null),
                new MenuItem("By capacity", actionFactory.inject(new ShowAvailableRoomsAction("capacity")), null),
                new MenuItem("By stars", actionFactory.inject(new ShowAvailableRoomsAction("stars")), null),
                new MenuItem("Back", actionFactory.get(BackAction.class))
        ));

        Menu bookingsCsvMenu = new Menu("CSV operations", List.of(
                new MenuItem("Import bookings from CSV", actionFactory.get(ImportBookingsAction.class), null),
                new MenuItem("Export bookings to CSV", actionFactory.get(ExportBookingsAction.class), null),
                new MenuItem("Back", actionFactory.get(BackAction.class))
        ));

        Menu bookingsMenu = new Menu(MenuTitles.BOOKINGS.get(), List.of(
                new MenuItem("Check in guest", actionFactory.get(CheckInAction.class), null),
                new MenuItem("Check out (by room)", actionFactory.get(CheckOutAction.class), null),
                new MenuItem("Show available rooms", actionFactory.inject(new NavigateAction(sortAvailableRoomsMenu)), sortAvailableRoomsMenu),
                new MenuItem("Count available rooms", actionFactory.get(ShowAvailableRoomCountAction.class), null),
                new MenuItem("Rooms free by date", actionFactory.get(ShowRoomsFreeByDateAction.class), null),
                new MenuItem("CSV import/export", actionFactory.inject(new NavigateAction(bookingsCsvMenu)), bookingsCsvMenu),
                new MenuItem("Back to main", actionFactory.get(BackAction.class))
        ));

        Menu sortAllRoomsMenu = new Menu("Sort all rooms", List.of(
                new MenuItem("By price", actionFactory.inject(new ShowAllRoomsAction("price")), null),
                new MenuItem("By capacity", actionFactory.inject(new ShowAllRoomsAction("capacity")), null),
                new MenuItem("By stars", actionFactory.inject(new ShowAllRoomsAction("stars")), null),
                new MenuItem("Back", actionFactory.get(BackAction.class))
        ));

        Menu roomsCsvMenu = new Menu("CSV operations", List.of(
                new MenuItem("Import rooms from CSV", actionFactory.get(ImportRoomsAction.class), null),
                new MenuItem("Export rooms to CSV", actionFactory.get(ExportRoomsAction.class), null),
                new MenuItem("Back", actionFactory.get(BackAction.class))
        ));

        Menu roomsMenu = new Menu(MenuTitles.ROOMS.get(), List.of(
                new MenuItem("Add room", actionFactory.get(AddRoomAction.class), null),
                new MenuItem("Set room maintenance", actionFactory.get(SetMaintenanceAction.class), null),
                new MenuItem("Change room price", actionFactory.get(ChangeRoomPriceAction.class), null),
                new MenuItem("Show room details", actionFactory.get(ShowRoomDetailsAction.class), null),
                new MenuItem("Show room history", actionFactory.get(ShowRoomHistoryAction.class), null),
                new MenuItem("Show all rooms", new NavigateAction(sortAllRoomsMenu), sortAllRoomsMenu),
                new MenuItem("CSV import/export", new NavigateAction(roomsCsvMenu), roomsCsvMenu),
                new MenuItem("Back to main", actionFactory.get(BackAction.class))
        ));

        Menu servicesCsvMenu = new Menu("CSV operations", List.of(
                new MenuItem("Import services from CSV", actionFactory.get(ImportServicesAction.class), null),
                new MenuItem("Export services to CSV", actionFactory.get(ExportServicesAction.class), null),
                new MenuItem("Back", actionFactory.get(BackAction.class))
        ));

        Menu servicesMenu = new Menu(MenuTitles.SERVICES.get(), List.of(
                new MenuItem("Add service", actionFactory.get(AddServiceAction.class), null),
                new MenuItem("Change service price", actionFactory.get(ChangeServicePriceAction.class), null),
                new MenuItem("Show all services", actionFactory.get(ShowAllServicesAction.class), null),
                new MenuItem("CSV import/export", actionFactory.inject(new NavigateAction(servicesCsvMenu)), servicesCsvMenu),
                new MenuItem("Back to main", actionFactory.get(BackAction.class))
        ));

        Menu sortGuestsMenu = new Menu("Sort guests", List.of(
                new MenuItem("By name", actionFactory.inject(new ShowGuestsAction("name")), null),
                new MenuItem("By checkout date", actionFactory.inject(new ShowGuestsAction("checkout")), null),
                new MenuItem("Back", actionFactory.get(BackAction.class))
        ));

        Menu sortGuestServicesMenu = new Menu("Sort guest services", List.of(
                new MenuItem("By price", actionFactory.inject(new ShowGuestServicesAction("price")), null),
                new MenuItem("By date", actionFactory.inject(new ShowGuestServicesAction("date")), null),
                new MenuItem("Back", actionFactory.get(BackAction.class))
        ));

        Menu guestsCsvMenu = new Menu("CSV operations", List.of(
                new MenuItem("Import guests from CSV", actionFactory.get(ImportGuestsAction.class), null),
                new MenuItem("Export guests to CSV", actionFactory.get(ExportGuestsAction.class), null),
                new MenuItem("Back", actionFactory.get(BackAction.class))
        ));

        Menu guestsMenu = new Menu(MenuTitles.GUESTS.get(), List.of(
                new MenuItem("Add guest", actionFactory.get(AddGuestAction.class), null),
                new MenuItem("Assign service to guest", actionFactory.get(AssignServiceToGuestAction.class), null),
                new MenuItem("Show guests", actionFactory.inject(new NavigateAction(sortGuestsMenu)), sortGuestsMenu),
                new MenuItem("Count current guests", actionFactory.get(ShowGuestCountAction.class), null),
                new MenuItem("Show guest bill", actionFactory.get(ShowGuestBillAction.class), null),
                new MenuItem("Show guest services", actionFactory.inject(new NavigateAction(sortGuestServicesMenu)), sortGuestServicesMenu),
                new MenuItem("CSV import/export", actionFactory.inject(new NavigateAction(guestsCsvMenu)), guestsCsvMenu),
                new MenuItem("Back to main", actionFactory.get(BackAction.class))
        ));

        Menu reportsMenu = new Menu(MenuTitles.REPORTS.get(), List.of(
                new MenuItem("Show prices overview", actionFactory.get(ShowAllPricesAction.class), null),
                new MenuItem("Back to main", actionFactory.get(BackAction.class))
        ));

        return new Menu(MenuTitles.MAIN.get(), List.of(
                new MenuItem("Bookings", actionFactory.inject(new NavigateAction(bookingsMenu)), bookingsMenu),
                new MenuItem("Rooms", actionFactory.inject(new NavigateAction(roomsMenu)), roomsMenu),
                new MenuItem("Services", actionFactory.inject(new NavigateAction(servicesMenu)), servicesMenu),
                new MenuItem("Guests", actionFactory.inject(new NavigateAction(guestsMenu)), guestsMenu),
                new MenuItem("Reports", actionFactory.inject(new NavigateAction(reportsMenu)), reportsMenu),
                new MenuItem("Exit", actionFactory.get(ExitAction.class), null)
        ));
    }
}
