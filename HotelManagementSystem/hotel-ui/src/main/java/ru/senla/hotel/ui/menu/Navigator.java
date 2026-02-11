package ru.senla.hotel.ui.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class Navigator {
    private Menu currentMenu;
    private final Deque<Menu> history = new ArrayDeque<>();

    private static final Logger log = LoggerFactory.getLogger(Navigator.class);

    public Navigator(Menu rootMenu) {
        this.currentMenu = rootMenu;
    }

    public void printMenu() {
        System.out.println("\n=== " + currentMenu.getName() + " ===");
        List<MenuItem> items = currentMenu.getMenuItems();
        for (int i = 0; i < items.size(); i++) {
            System.out.println((i + 1) + ". " + items.get(i).getTitle());
        }
        System.out.print("Select option (number): ");
    }

    public void navigate(int index) {
        log.info("User navigation started: menu={}, selection={}", currentMenu.getName(), index);

        List<MenuItem> items = currentMenu.getMenuItems();

        if (index < 1 || index > items.size()) {
            log.error("Invalid menu selection: menu={}, selection={}", currentMenu.getName(), index);
            System.out.println("Invalid selection. Please choose a valid menu item.");
            return;
        }

        MenuItem item = items.get(index - 1);
        try {
            item.doAction();
        } catch (Exception e) {
            log.error("Error during action execution: menu={}, selection={}", currentMenu.getName(), index, e);
            System.out.println("Error: " + e.getMessage());
            return;
        }

        if (item.isBack()) {
            if (!history.isEmpty()) {
                currentMenu = history.pop();
                log.info("Returned to previous menu: {}", currentMenu.getName());
            }
            return;
        }

        if (item.getNextMenu() != null) {
            history.push(currentMenu);
            currentMenu = item.getNextMenu();
            log.info("Navigation completed: current menu={}", currentMenu.getName());
        }
    }
}
