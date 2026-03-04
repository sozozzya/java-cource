package ru.senla.hotel.presentation.console.builder;

import org.springframework.stereotype.Component;
import ru.senla.hotel.presentation.console.actions.ExitAction;
import ru.senla.hotel.presentation.console.actions.IAction;
import ru.senla.hotel.presentation.console.actions.NavigateAction;
import ru.senla.hotel.presentation.console.actions.BackAction;
import ru.senla.hotel.presentation.console.actions.bookings.ShowAvailableRoomCountAction;
import ru.senla.hotel.presentation.console.actions.bookings.ShowAvailableRoomsAction;
import ru.senla.hotel.presentation.console.actions.bookings.ShowRoomsFreeByDateAction;
import ru.senla.hotel.presentation.console.actions.bookings.CheckInAction;
import ru.senla.hotel.presentation.console.actions.bookings.CheckOutAction;
import ru.senla.hotel.presentation.console.actions.bookings.ExportBookingsAction;
import ru.senla.hotel.presentation.console.actions.bookings.ImportBookingsAction;
import ru.senla.hotel.presentation.console.actions.rooms.ImportRoomsAction;
import ru.senla.hotel.presentation.console.actions.rooms.SetMaintenanceAction;
import ru.senla.hotel.presentation.console.actions.rooms.ShowAllRoomsAction;
import ru.senla.hotel.presentation.console.actions.rooms.ShowRoomDetailsAction;
import ru.senla.hotel.presentation.console.actions.rooms.ShowRoomHistoryAction;
import ru.senla.hotel.presentation.console.actions.rooms.AddRoomAction;
import ru.senla.hotel.presentation.console.actions.rooms.ChangeRoomPriceAction;
import ru.senla.hotel.presentation.console.actions.rooms.ExportRoomsAction;
import ru.senla.hotel.presentation.console.actions.reports.ShowAllPricesAction;
import ru.senla.hotel.presentation.console.actions.services.ExportServicesAction;
import ru.senla.hotel.presentation.console.actions.services.ImportServicesAction;
import ru.senla.hotel.presentation.console.actions.services.AddServiceAction;
import ru.senla.hotel.presentation.console.actions.services.ShowAllServicesAction;
import ru.senla.hotel.presentation.console.actions.services.ChangeServicePriceAction;
import ru.senla.hotel.presentation.console.actions.guests.AddGuestAction;
import ru.senla.hotel.presentation.console.actions.guests.AssignServiceToGuestAction;
import ru.senla.hotel.presentation.console.actions.guests.ImportGuestsAction;
import ru.senla.hotel.presentation.console.actions.guests.ExportGuestsAction;
import ru.senla.hotel.presentation.console.actions.guests.ShowGuestBillAction;
import ru.senla.hotel.presentation.console.actions.guests.ShowGuestCountAction;
import ru.senla.hotel.presentation.console.actions.guests.ShowGuestsAction;
import ru.senla.hotel.presentation.console.actions.guests.ShowGuestServicesAction;
import ru.senla.hotel.presentation.console.menu.Menu;
import ru.senla.hotel.presentation.console.menu.MenuItem;
import ru.senla.hotel.presentation.console.util.ActionFactory;
import ru.senla.hotel.presentation.console.util.MenuTitles;

import java.util.List;

@Component
public class ConsoleMenuFactory implements AbstractMenuFactory {

    private final ActionFactory actions;

    public ConsoleMenuFactory(ActionFactory actions) {
        this.actions = actions;
    }

    private IAction createNavigateAction(Menu menu) {
        NavigateAction action = actions.get(NavigateAction.class);
        action.setMenu(menu);
        return action;
    }

    @Override
    public Menu createRootMenu() {

        Menu sortAvailableRoomsMenu = new Menu("Sort available rooms", List.of(
                new MenuItem("By price", () -> {
                    ShowAvailableRoomsAction action = actions.get(ShowAvailableRoomsAction.class);
                    action.setSortBy("price");
                    return action;
                }, null),
                new MenuItem("By capacity", () -> {
                    ShowAvailableRoomsAction action = actions.get(ShowAvailableRoomsAction.class);
                    action.setSortBy("capacity");
                    return action;
                }, null),
                new MenuItem("By stars", () -> {
                    ShowAvailableRoomsAction action = actions.get(ShowAvailableRoomsAction.class);
                    action.setSortBy("stars");
                    return action;
                }, null),
                new MenuItem("Back",
                        () -> actions.get(BackAction.class))
        ));

        Menu bookingsCsvMenu = new Menu("CSV operations", List.of(
                new MenuItem("Import bookings from CSV",
                        () -> actions.get(ImportBookingsAction.class), null),
                new MenuItem("Export bookings to CSV",
                        () -> actions.get(ExportBookingsAction.class), null),
                new MenuItem("Back",
                        () -> actions.get(BackAction.class))
        ));

        Menu bookingsMenu = new Menu(MenuTitles.BOOKINGS.get(), List.of(
                new MenuItem("Check in guest",
                        () -> actions.get(CheckInAction.class), null),
                new MenuItem("Check out (by room)",
                        () -> actions.get(CheckOutAction.class), null),
                new MenuItem("Show available rooms",
                        () -> createNavigateAction(sortAvailableRoomsMenu), sortAvailableRoomsMenu),
                new MenuItem("Count available rooms",
                        () -> actions.get(ShowAvailableRoomCountAction.class), null),
                new MenuItem("Rooms free by date",
                        () -> actions.get(ShowRoomsFreeByDateAction.class), null),
                new MenuItem("CSV import/export",
                        () -> createNavigateAction(bookingsCsvMenu), bookingsCsvMenu),
                new MenuItem("Back to main",
                        () -> actions.get(BackAction.class))
        ));

        Menu sortAllRoomsMenu = new Menu("Sort all rooms", List.of(
                new MenuItem("By price", () -> {
                    ShowAllRoomsAction action = actions.get(ShowAllRoomsAction.class);
                    action.setSortBy("price");
                    return action;
                }, null),
                new MenuItem("By capacity", () -> {
                    ShowAllRoomsAction action = actions.get(ShowAllRoomsAction.class);
                    action.setSortBy("capacity");
                    return action;
                }, null),
                new MenuItem("By stars", () -> {
                    ShowAllRoomsAction action = actions.get(ShowAllRoomsAction.class);
                    action.setSortBy("stars");
                    return action;
                }, null),
                new MenuItem("Back",
                        () -> actions.get(BackAction.class))
        ));

        Menu roomsCsvMenu = new Menu("CSV operations", List.of(
                new MenuItem("Import rooms from CSV",
                        () -> actions.get(ImportRoomsAction.class), null),
                new MenuItem("Export rooms to CSV",
                        () -> actions.get(ExportRoomsAction.class), null),
                new MenuItem("Back",
                        () -> actions.get(BackAction.class))
        ));

        Menu roomsMenu = new Menu(MenuTitles.ROOMS.get(), List.of(
                new MenuItem("Add room",
                        () -> actions.get(AddRoomAction.class), null),
                new MenuItem("Set room maintenance",
                        () -> actions.get(SetMaintenanceAction.class), null),
                new MenuItem("Change room price",
                        () -> actions.get(ChangeRoomPriceAction.class), null),
                new MenuItem("Show room details",
                        () -> actions.get(ShowRoomDetailsAction.class), null),
                new MenuItem("Show room history",
                        () -> actions.get(ShowRoomHistoryAction.class), null),
                new MenuItem("Show all rooms",
                        () -> createNavigateAction(sortAllRoomsMenu), sortAllRoomsMenu),
                new MenuItem("CSV import/export",
                        () -> createNavigateAction(roomsCsvMenu), roomsCsvMenu),
                new MenuItem("Back to main",
                        () -> actions.get(BackAction.class))
        ));

        Menu servicesCsvMenu = new Menu("CSV operations", List.of(
                new MenuItem("Import services from CSV",
                        () -> actions.get(ImportServicesAction.class), null),
                new MenuItem("Export services to CSV",
                        () -> actions.get(ExportServicesAction.class), null),
                new MenuItem("Back",
                        () -> actions.get(BackAction.class))
        ));

        Menu servicesMenu = new Menu(MenuTitles.SERVICES.get(), List.of(
                new MenuItem("Add service",
                        () -> actions.get(AddServiceAction.class), null),
                new MenuItem("Change service price",
                        () -> actions.get(ChangeServicePriceAction.class), null),
                new MenuItem("Show all services",
                        () -> actions.get(ShowAllServicesAction.class), null),
                new MenuItem("CSV import/export",
                        () -> createNavigateAction(servicesCsvMenu), servicesCsvMenu),
                new MenuItem("Back to main",
                        () -> actions.get(BackAction.class))
        ));

        Menu sortGuestsMenu = new Menu("Sort guests", List.of(
                new MenuItem("By name", () -> {
                    ShowGuestsAction action = actions.get(ShowGuestsAction.class);
                    action.setSortBy("name");
                    return action;
                }, null),
                new MenuItem("By checkout date", () -> {
                    ShowGuestsAction action = actions.get(ShowGuestsAction.class);
                    action.setSortBy("checkout");
                    return action;
                }, null),
                new MenuItem("Back",
                        () -> actions.get(BackAction.class))
        ));

        Menu sortGuestServicesMenu = new Menu("Sort guest services", List.of(
                new MenuItem("By price", () -> {
                    ShowGuestServicesAction action = actions.get(ShowGuestServicesAction.class);
                    action.setSortBy("price");
                    return action;
                }, null),
                new MenuItem("By date", () -> {
                    ShowGuestServicesAction action = actions.get(ShowGuestServicesAction.class);
                    action.setSortBy("date");
                    return action;
                }, null),
                new MenuItem("Back",
                        () -> actions.get(BackAction.class))
        ));

        Menu guestsCsvMenu = new Menu("CSV operations", List.of(
                new MenuItem("Import guests from CSV",
                        () -> actions.get(ImportGuestsAction.class), null),
                new MenuItem("Export guests to CSV",
                        () -> actions.get(ExportGuestsAction.class), null),
                new MenuItem("Back",
                        () -> actions.get(BackAction.class))
        ));

        Menu guestsMenu = new Menu(MenuTitles.GUESTS.get(), List.of(
                new MenuItem("Add guest",
                        () -> actions.get(AddGuestAction.class), null),
                new MenuItem("Assign service to guest",
                        () -> actions.get(AssignServiceToGuestAction.class), null),
                new MenuItem("Show guests",
                        () -> createNavigateAction(sortGuestsMenu), sortGuestsMenu),
                new MenuItem("Count current guests",
                        () -> actions.get(ShowGuestCountAction.class), null),
                new MenuItem("Show guest bill",
                        () -> actions.get(ShowGuestBillAction.class), null),
                new MenuItem("Show guest services",
                        () -> createNavigateAction(sortGuestServicesMenu), sortGuestServicesMenu),
                new MenuItem("CSV import/export",
                        () -> createNavigateAction(guestsCsvMenu), guestsCsvMenu),
                new MenuItem("Back to main",
                        () -> actions.get(BackAction.class))
        ));

        Menu reportsMenu = new Menu(MenuTitles.REPORTS.get(), List.of(
                new MenuItem("Show prices overview",
                        () -> actions.get(ShowAllPricesAction.class), null),
                new MenuItem("Back to main",
                        () -> actions.get(BackAction.class))
        ));

        return new Menu(MenuTitles.MAIN.get(), List.of(
                new MenuItem("Bookings",
                        () -> createNavigateAction(bookingsMenu), bookingsMenu),
                new MenuItem("Rooms",
                        () -> createNavigateAction(roomsMenu), roomsMenu),
                new MenuItem("Services",
                        () -> createNavigateAction(servicesMenu), servicesMenu),
                new MenuItem("Guests",
                        () -> createNavigateAction(guestsMenu), guestsMenu),
                new MenuItem("Reports",
                        () -> createNavigateAction(reportsMenu), reportsMenu),
                new MenuItem("Exit",
                        () -> actions.get(ExitAction.class), null)
        ));
    }
}
