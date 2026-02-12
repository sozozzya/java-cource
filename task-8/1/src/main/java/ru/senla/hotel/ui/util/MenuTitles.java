package ru.senla.hotel.ui.util;

public enum MenuTitles {
    MAIN("Main Menu"),
    ROOMS("Rooms"),
    BOOKINGS("Bookings"),
    GUESTS("Guests"),
    SERVICES("Services"),
    REPORTS("Reports");

    private final String title;

    MenuTitles(String title) {
        this.title = title;
    }

    public String get() {
        return title;
    }
}
