package ru.senla.hotel.presentation.console.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.senla.hotel.presentation.console.builder.Builder;
import ru.senla.hotel.presentation.console.menu.Menu;
import ru.senla.hotel.presentation.console.menu.Navigator;
import ru.senla.hotel.presentation.console.util.ConsoleReader;

@Component
public class MenuController {

    private static final Logger log = LoggerFactory.getLogger(MenuController.class);

    private final Builder builder;
    private final ConsoleReader reader;

    private Navigator navigator;

    public MenuController(Builder builder,
                          ConsoleReader reader) {
        this.builder = builder;
        this.reader = reader;
    }

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
