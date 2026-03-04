package ru.senla.hotel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.hotel.ui.controller.MenuController;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        log.info("Application starting");

        try {
            ApplicationBootstrap context = new ApplicationBootstrap();

            context.getBean(MenuController.class).run();

            log.info("Application finished successfully");
        } catch (Exception e) {
            log.error("Fatal error. Application terminated", e);
        }
    }
}
