package ru.senla.hotel.ui.controller;

import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.builder.Builder;
import ru.senla.hotel.ui.menu.Menu;
import ru.senla.hotel.ui.menu.Navigator;
import ru.senla.hotel.ui.util.ConsoleReader;

public class MenuController {
    private final Navigator navigator;

    public MenuController(Administrator admin) {
        Builder builder = new Builder(admin);
        Menu root = builder.buildRootMenu();
        this.navigator = new Navigator(root);
    }

    public void run() {
        ConsoleReader reader = ConsoleReader.getInstance();
        while (true) {
            navigator.printMenu();
            String line = reader.nextLine().trim();
            try {
                int choice = Integer.parseInt(line);
                navigator.navigate(choice);
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a number.");
            }
        }
    }
}