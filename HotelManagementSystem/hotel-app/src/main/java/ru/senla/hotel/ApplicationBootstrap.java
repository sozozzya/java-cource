package ru.senla.hotel;

import ru.senla.hotel.autoconfig.processor.ConfigProcessor;
import ru.senla.hotel.dao.jdbc.JdbcBookingDAO;
import ru.senla.hotel.dao.jdbc.JdbcGuestDAO;
import ru.senla.hotel.dao.jdbc.JdbcRoomDAO;
import ru.senla.hotel.dao.jdbc.JdbcServiceDAO;
import ru.senla.hotel.db.ConnectionManager;
import ru.senla.hotel.di.processor.DependencyInjector;
import ru.senla.hotel.service.Administrator;
import ru.senla.hotel.service.ServiceManager;
import ru.senla.hotel.service.BookingManager;
import ru.senla.hotel.service.GuestManager;
import ru.senla.hotel.service.ReportManager;
import ru.senla.hotel.service.RoomManager;
import ru.senla.hotel.ui.actions.BackAction;
import ru.senla.hotel.ui.actions.ExitAction;
import ru.senla.hotel.ui.actions.bookings.ShowAvailableRoomCountAction;
import ru.senla.hotel.ui.actions.bookings.ShowRoomsFreeByDateAction;
import ru.senla.hotel.ui.actions.bookings.CheckInAction;
import ru.senla.hotel.ui.actions.bookings.CheckOutAction;
import ru.senla.hotel.ui.actions.bookings.ExportBookingsAction;
import ru.senla.hotel.ui.actions.bookings.ImportBookingsAction;
import ru.senla.hotel.ui.actions.rooms.ImportRoomsAction;
import ru.senla.hotel.ui.actions.rooms.SetMaintenanceAction;
import ru.senla.hotel.ui.actions.rooms.ShowRoomDetailsAction;
import ru.senla.hotel.ui.actions.rooms.ShowRoomHistoryAction;
import ru.senla.hotel.ui.actions.rooms.AddRoomAction;
import ru.senla.hotel.ui.actions.rooms.ChangeRoomPriceAction;
import ru.senla.hotel.ui.actions.rooms.ExportRoomsAction;
import ru.senla.hotel.ui.actions.reports.ShowAllPricesAction;
import ru.senla.hotel.ui.actions.services.ExportServicesAction;
import ru.senla.hotel.ui.actions.services.ImportServicesAction;
import ru.senla.hotel.ui.actions.services.AddServiceAction;
import ru.senla.hotel.ui.actions.services.ShowAllServicesAction;
import ru.senla.hotel.ui.actions.services.ChangeServicePriceAction;
import ru.senla.hotel.ui.actions.guests.AddGuestAction;
import ru.senla.hotel.ui.actions.guests.AssignServiceToGuestAction;
import ru.senla.hotel.ui.actions.guests.ImportGuestsAction;
import ru.senla.hotel.ui.actions.guests.ExportGuestsAction;
import ru.senla.hotel.ui.actions.guests.ShowGuestBillAction;
import ru.senla.hotel.ui.actions.guests.ShowGuestCountAction;
import ru.senla.hotel.ui.builder.Builder;
import ru.senla.hotel.ui.builder.ConsoleMenuFactory;
import ru.senla.hotel.ui.controller.MenuController;
import ru.senla.hotel.ui.util.ActionFactory;
import ru.senla.hotel.ui.util.ConsoleReader;

public class ApplicationBootstrap {

    private final DependencyInjector injector;

    public ApplicationBootstrap() {
        ApplicationConfig config = new ApplicationConfig();
        ConfigProcessor processor = new ConfigProcessor();
        processor.process(config);

        this.injector = new DependencyInjector();

        injector.registerInstance(ApplicationConfig.class, config);

        registerBeans();

        injector.initialize();
    }

    private void registerBeans() {
        injector.register(
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

        injector.registerPrototype(
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
