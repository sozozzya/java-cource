package ru.senla.task3.hotel;

import ru.senla.task3.hotel.management.Administrator;
import ru.senla.task3.hotel.model.Room;
import ru.senla.task3.hotel.model.Service;

public class Main {
    public static void main(String[] args) {
        Administrator admin = new Administrator();

        // --- Initialization ---
        admin.addRoom(new Room(101, 2500));
        admin.addRoom(new Room(102, 3000));

        admin.addService(new Service("Breakfast", 500));
        admin.addService(new Service("Cleaning", 300));

        // --- Basic operations ---
        admin.checkIn(101);                            // Check in guest to 101
        admin.setRoomMaintenance(102, true);    // Put 102 under maintenance
        admin.checkIn(102);                           // Fail: under maintenance
        admin.checkOut(101);                          // Check out guest from 101

        // --- Price updates ---
        admin.changeRoomPrice(101, 2700);
        admin.changeServicePrice("Breakfast", 600);

        // --- Extended test cases ---
        System.out.println("\n--- Additional Tests ---");

        admin.checkIn(101);       // Check in again (after checkout)
        admin.checkIn(101);       // Fail: already occupied
        admin.checkOut(101);      // Checkout again
        admin.checkOut(101);      // Fail: already vacant

        admin.setRoomMaintenance(102, false);   // Mark as available again
        admin.checkIn(102);                           // Should succeed now
        admin.setRoomMaintenance(102, true);    // Should fail gracefully (room occupied)
        admin.checkOut(102);                           // Free room 102
        admin.setRoomMaintenance(102, true);    // Now can set maintenance
        admin.checkIn(102);                            // Fail: under maintenance

        // --- Invalid lookups ---
        admin.changeRoomPrice(999, 1500);       // Room not found
        admin.changeServicePrice("Spa", 800);   // Service not found

        // --- Final state overview ---
        System.out.println("\n--- Final State ---");
        admin.printAllRooms();
        admin.printAllServices();
    }
}