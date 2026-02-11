package ru.senla.hotel.management;

import ru.senla.hotel.model.Room;
import ru.senla.hotel.model.Service;

public class PriceManager {
    public void changeRoomPrice(Room room, double newPrice) {
        if (newPrice < 0) {
            System.out.println("Negative price value for room " + room.getNumber() + ".");
            return;
        }
        room.setPricePerNight(newPrice);
        System.out.println("Room " + room.getNumber() + " price updated on " + newPrice + " ₽.");
    }

    public void changeServicePrice(Service service, double newPrice) {
        if (newPrice < 0) {
            System.out.println("Negative price value for service " + service.getName() + ".");
            return;
        }
        service.setPrice(newPrice);
        System.out.println("Service " + service.getName() + " price updated on " + newPrice + " ₽.");
    }
}
