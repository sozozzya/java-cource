package ru.senla.hotel.ui.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.ui.builder.Builder;
import ru.senla.hotel.ui.menu.Menu;
import ru.senla.hotel.ui.menu.Navigator;
import ru.senla.hotel.ui.util.ConsoleReader;

@Component
public class MenuController {

    @Inject
    private Builder builder;

    @Inject
    private ConsoleReader reader;

    private Navigator navigator;

    private static final Logger log = LoggerFactory.getLogger(MenuController.class);

    public void run() {
        if (navigator == null) {
            Menu root = builder.buildRootMenu();
            this.navigator = new Navigator(root);
            log.info("Application started, root menu initialized");
        }
        while (true) {
            try {
                navigator.printMenu();
                String line = reader.nextLine().trim();
                int choice = Integer.parseInt(line);

                log.info("User selected menu option: {}", choice);
                navigator.navigate(choice);

                log.info("Menu option {} processed successfully", choice);
            } catch (NumberFormatException e) {
                log.error("Invalid menu input", e);
                System.out.println("Please enter a valid number.");
            } catch (Exception e) {
                log.error("Unexpected error during command processing", e);
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
