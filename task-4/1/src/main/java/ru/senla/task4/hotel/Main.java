package ru.senla.task4.hotel;

import ru.senla.task4.hotel.management.Administrator;
import ru.senla.task4.hotel.model.Guest;
import ru.senla.task4.hotel.model.Room;
import ru.senla.task4.hotel.model.Service;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Administrator admin = new Administrator();

        System.out.println("===== INITIALIZATION =====");
        admin.addRoom(new Room(101, 2, 3, 3500));
        admin.addRoom(new Room(102, 4, 4, 5500));
        admin.addRoom(new Room(103, 1, 2, 2500));
        admin.addRoom(new Room(104, 2, 3, 4000));
        admin.addRoom(new Room(105, 3, 4, 4800));
        admin.addRoom(new Room(106, 1, 1, 2000));
        admin.addRoom(new Room(107, 2, 2, 3000));
        System.out.println();

        admin.addService(new Service("Breakfast", 500));
        admin.addService(new Service("Cleaning", 300));
        admin.addService(new Service("SPA", 1200));

        Guest ivan = new Guest("Ivan Ivanov");
        Guest maria = new Guest("Maria Petrova");
        Guest sergey = new Guest("Sergey Kuznetsov");
        Guest paul = new Guest("Paul Sidorov");
        Guest anna = new Guest("Anna Smirnova");

        System.out.println("\n===== CHECK-IN / CHECK-OUT TESTS =====");
        admin.checkIn(ivan, 101, LocalDate.now(), LocalDate.now().plusDays(3));
        admin.checkIn(maria, 101, LocalDate.now(), LocalDate.now().plusDays(5));
        admin.checkIn(maria, 102, LocalDate.now(), LocalDate.now().plusDays(4));
        admin.checkIn(sergey, 103, LocalDate.now(), LocalDate.now().plusDays(2));

        admin.checkOut(101);
        admin.checkOut(101);
        admin.checkOut(103);
        admin.checkOut(105);

        admin.checkIn(ivan, 106, LocalDate.now(), LocalDate.now().plusDays(2));
        admin.checkIn(paul, 101, LocalDate.now(), LocalDate.now().plusDays(6));

        admin.checkOut(101);
        admin.checkIn(anna, 101, LocalDate.now(), LocalDate.now().plusDays(7));

        System.out.println("\n===== SERVICE ASSIGNMENT TESTS =====");
        admin.assignServiceToGuest(ivan, "Breakfast", LocalDate.now());
        admin.assignServiceToGuest(ivan, "SPA", LocalDate.now().plusDays(1));
        admin.assignServiceToGuest(maria, "Cleaning", LocalDate.now().plusDays(2));
        admin.assignServiceToGuest(sergey, "SPA", LocalDate.now());
        admin.assignServiceToGuest(sergey, "Breakfast", LocalDate.now().plusDays(1));
        admin.assignServiceToGuest(anna, "Cleaning", LocalDate.now());

        System.out.println("\n===== ROOM STATUS TESTS =====");
        admin.setRoomMaintenance(107, true);
        admin.checkIn(new Guest("Dmitry Ivanov"), 107, LocalDate.now(), LocalDate.now().plusDays(2)); // не получится
        admin.setRoomMaintenance(107, false);
        admin.checkIn(new Guest("Dmitry Ivanov"), 107, LocalDate.now(), LocalDate.now().plusDays(2)); // теперь заселение успешно

        System.out.println("\n===== PRICE UPDATE TESTS =====");
        admin.changeRoomPrice(102, 6000);
        admin.changeRoomPrice(110, 2000);
        admin.changeServicePrice("SPA", 1500);
        admin.changeServicePrice("Transfer", 800);

        System.out.println("\n===== REPORT TESTS =====");
        System.out.println("----- ALL ROOMS (sorted by price) -----");
        admin.printAllRoomsByPrice();

        System.out.println("\n----- ALL ROOMS (sorted by capacity) -----");
        admin.printAllRoomsByCapacity();

        System.out.println("\n----- ALL ROOMS (sorted by stars) -----");
        admin.printAllRoomsByStars();

        System.out.println("\n----- AVAILABLE ROOMS (sorted by price) -----");
        admin.printAvailableRoomsByPrice();

        System.out.println("\n----- AVAILABLE ROOMS (sorted by capacity) -----");
        admin.printAvailableRoomsByCapacity();

        System.out.println("\n----- AVAILABLE ROOMS (sorted by stars) -----");
        admin.printAvailableRoomsByStars();

        System.out.println("\n----- GUESTS (sorted by name) -----");
        admin.printGuestsByName();

        System.out.println("\n----- GUESTS (sorted by checkout date) -----");
        admin.printGuestsByCheckoutDate();
        System.out.println();

        admin.printAvailableRoomCount();
        admin.printGuestCount();
        System.out.println();

        admin.printRoomsFreeByDate(LocalDate.now().plusDays(3));

        System.out.println("\n===== ROOM DETAILS =====");
        admin.printRoomDetails(102);
        System.out.println();
        admin.printRoomDetails(103);

        System.out.println("\n===== ROOM HISTORY =====");
        admin.printRoomHistory(101);
        System.out.println();
        admin.printRoomHistory(106);

        System.out.println("\n===== GUESTS BILLS =====");
        admin.printGuestBill(sergey);
        System.out.println();
        admin.printGuestBill(paul);

        System.out.println("\n===== GUESTS SERVICES =====");
        admin.printGuestServicesByPrice(ivan);
        System.out.println();
        admin.printGuestServicesByDate(anna);

        System.out.println("\n===== PRICES OVERVIEW =====");
        admin.printAllPrices();

        System.out.println("\n===== INVALID LOOKUP TESTS =====");
        admin.changeRoomPrice(999, 2500);
        admin.printRoomDetails(999);

        System.out.println("\n===== END OF TESTS =====");
    }
}
