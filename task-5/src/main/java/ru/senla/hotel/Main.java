package ru.senla.hotel;

import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.controller.MenuController;

public class Main {
    public static void main(String[] args) {
        Administrator admin = new Administrator();

        MenuController controller = new MenuController(admin);
        controller.run();
    }
}
