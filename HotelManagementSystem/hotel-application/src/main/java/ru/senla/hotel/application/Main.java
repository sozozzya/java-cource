package ru.senla.hotel.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.senla.hotel.config.SpringConfig;
import ru.senla.hotel.presentation.console.controller.MenuController;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        log.info("Application starting");

        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(SpringConfig.class)) {

            MenuController controller = context.getBean(MenuController.class);
            controller.run();

            log.info("Application finished successfully");
        } catch (Exception e) {
            log.error("Fatal error. Application terminated", e);
        }
    }
}
