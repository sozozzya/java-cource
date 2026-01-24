package ru.senla.hotel;

import ru.senla.hotel.autoconfig.processor.ConfigProcessor;
import ru.senla.hotel.config.ApplicationConfig;
import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.controller.MenuController;

public class Main {
    public static void main(String[] args) {
        try {
            ApplicationConfig config = new ApplicationConfig();

            ConfigProcessor processor = new ConfigProcessor();
            processor.configure(config);

            Administrator admin = new Administrator(config);

            admin.loadAppState();
            Runtime.getRuntime().addShutdownHook(new Thread(admin::saveAppState));

            MenuController controller = new MenuController(admin);
            controller.run();
        } catch (Exception e) {
            System.out.println("Fatal error: " + e.getMessage());
        }
    }
}
