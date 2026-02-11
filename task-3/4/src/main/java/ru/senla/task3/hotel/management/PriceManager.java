package ru.senla.task3.hotel.management;

import ru.senla.task3.hotel.model.Room;
import ru.senla.task3.hotel.model.Service;

public class PriceManager {

    public void changeRoomPrice(Room room, double newPrice) {
        room.setPricePerNight(newPrice);
        System.out.println("Room " + room.getNumber() + " price has been changed to " + newPrice + ".\n");
    }

    public void changeServicePrice(Service service, double newPrice) {
        service.setPrice(newPrice);
        System.out.println("Service \"" + service.getName() + "\" price has been changed to " + newPrice + ".\n");
    }
}
