package ru.senla.hotel.ui.controller;

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

    public void run() {
        if (navigator == null) {
            Menu root = builder.buildRootMenu();
            this.navigator = new Navigator(root);
        }
        while (true) {
            try {
                navigator.printMenu();
                String line = reader.nextLine().trim();
                int choice = Integer.parseInt(line);
                navigator.navigate(choice);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
