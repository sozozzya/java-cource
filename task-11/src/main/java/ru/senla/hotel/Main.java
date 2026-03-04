package ru.senla.hotel;

import ru.senla.hotel.di.context.DIContext;
import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.controller.MenuController;

public class Main {

    public static void main(String[] args) {
        try {
            DIContext context = new DIContext();

            context.getBean(MenuController.class).run();

        } catch (Exception e) {
            System.out.println("Fatal error: " + e.getMessage());
        }
    }
}
