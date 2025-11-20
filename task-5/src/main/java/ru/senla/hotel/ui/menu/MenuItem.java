package ru.senla.hotel.ui.menu;

public class MenuItem {
    private final String title;
    private final IAction action;
    private final Menu nextMenu;
    private final boolean isBack;

    public MenuItem(String title, IAction action, Menu nextMenu) {
        this(title, action, nextMenu, false);
    }

    public MenuItem(String title, IAction action) {
        this(title, action, null, true);
    }

    private MenuItem(String title, IAction action, Menu nextMenu, boolean isBack) {
        this.title = title;
        this.action = action;
        this.nextMenu = nextMenu;
        this.isBack = isBack;
    }

    public String getTitle() {
        return title;
    }

    public Menu getNextMenu() {
        return nextMenu;
    }

    public boolean isBack() {
        return isBack;
    }

    public void doAction() {
        if (action != null) {
            action.execute();
        }
    }
}

