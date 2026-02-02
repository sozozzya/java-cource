package ru.senla.hotel.di.context;

import ru.senla.hotel.autoconfig.processor.ConfigProcessor;
import ru.senla.hotel.config.ApplicationConfig;
import ru.senla.hotel.dao.jdbc.JdbcBookingDAO;
import ru.senla.hotel.dao.jdbc.JdbcGuestDAO;
import ru.senla.hotel.dao.jdbc.JdbcRoomDAO;
import ru.senla.hotel.dao.jdbc.JdbcServiceDAO;
import ru.senla.hotel.db.ConnectionManager;
import ru.senla.hotel.di.processor.DependencyInjector;
import ru.senla.hotel.management.*;
import ru.senla.hotel.ui.actions.BackAction;
import ru.senla.hotel.ui.actions.ExitAction;
import ru.senla.hotel.ui.actions.bookings.*;
import ru.senla.hotel.ui.actions.guests.*;
import ru.senla.hotel.ui.actions.reports.ShowAllPricesAction;
import ru.senla.hotel.ui.actions.rooms.*;
import ru.senla.hotel.ui.actions.services.*;
import ru.senla.hotel.ui.builder.Builder;
import ru.senla.hotel.ui.builder.ConsoleMenuFactory;
import ru.senla.hotel.ui.controller.MenuController;
import ru.senla.hotel.ui.util.ActionFactory;
import ru.senla.hotel.ui.util.ConsoleReader;

public class DIContext {

    private final DependencyInjector injector;

    public DIContext() {
        this.injector = new DependencyInjector();
        registerBeans();
        injector.initialize();
    }

    private void registerBeans() {
        injector.register(
                ConfigProcessor.class,
                ApplicationConfig.class,

                RoomManager.class,
                GuestManager.class,
                ServiceManager.class,
                BookingManager.class,
                ReportManager.class,

                JdbcRoomDAO.class,
                JdbcGuestDAO.class,
                JdbcServiceDAO.class,
                JdbcBookingDAO.class,

                ConnectionManager.class,

                Builder.class,
                ActionFactory.class,
                ConsoleMenuFactory.class,
                ConsoleReader.class,
                MenuController.class,

                Administrator.class
        );

        injector.registerActions(
                BackAction.class,
                ExitAction.class,

                ImportBookingsAction.class,
                ExportBookingsAction.class,
                CheckInAction.class,
                CheckOutAction.class,
                ShowAvailableRoomCountAction.class,
                ShowRoomsFreeByDateAction.class,

                ImportRoomsAction.class,
                ExportRoomsAction.class,
                AddRoomAction.class,
                SetMaintenanceAction.class,
                ChangeRoomPriceAction.class,
                ShowRoomDetailsAction.class,
                ShowRoomHistoryAction.class,

                ImportGuestsAction.class,
                ExportGuestsAction.class,
                AddGuestAction.class,
                AssignServiceToGuestAction.class,
                ShowGuestBillAction.class,
                ShowGuestCountAction.class,

                ImportServicesAction.class,
                ExportServicesAction.class,
                AddServiceAction.class,
                ChangeServicePriceAction.class,
                ShowAllServicesAction.class,

                ShowAllPricesAction.class
        );
    }

    public <T> T getBean(Class<T> type) {
        return injector.getBean(type);
    }
}
