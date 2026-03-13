package ru.senla.hotel.presentation.mapper;

import org.springframework.stereotype.Component;
import ru.senla.hotel.domain.entity.Room;
import ru.senla.hotel.domain.entity.StayRecord;
import ru.senla.hotel.presentation.dto.request.room.CreateRoomRequest;
import ru.senla.hotel.presentation.dto.response.room.RoomDetailsResponse;
import ru.senla.hotel.presentation.dto.response.room.RoomLastGuestsResponse;
import ru.senla.hotel.presentation.dto.response.room.RoomPriceResponse;
import ru.senla.hotel.presentation.dto.response.room.RoomResponse;

@Component
public class RoomMapper {

    public RoomResponse toResponse(Room room) {

        RoomResponse dto = new RoomResponse();

        dto.setId(room.getId());
        dto.setNumber(room.getNumber());
        dto.setCapacity(room.getCapacity());
        dto.setStars(room.getStars());
        dto.setPricePerNight(room.getPricePerNight());
        dto.setUnderMaintenance(room.isUnderMaintenance());

        return dto;
    }

    public RoomPriceResponse toPriceResponse(Room room) {

        RoomPriceResponse dto = new RoomPriceResponse();

        dto.setRoomNumber(room.getNumber());
        dto.setPrice(room.getPricePerNight());

        return dto;
    }

    public RoomLastGuestsResponse toLastGuestsResponse(StayRecord record) {

        RoomLastGuestsResponse dto = new RoomLastGuestsResponse();

        dto.setGuestName(record.getGuestName());
        dto.setCheckIn(record.getCheckIn());
        dto.setCheckOut(record.getCheckOut());

        return dto;
    }

    public RoomDetailsResponse toDetails(Room room) {

        RoomDetailsResponse dto = new RoomDetailsResponse();

        dto.setId(room.getId());
        dto.setNumber(room.getNumber());
        dto.setCapacity(room.getCapacity());
        dto.setStars(room.getStars());
        dto.setPricePerNight(room.getPricePerNight());
        dto.setUnderMaintenance(room.isUnderMaintenance());

        return dto;
    }

    public Room toEntity(CreateRoomRequest request) {

        Room room = new Room();

        room.setNumber(request.getNumber());
        room.setCapacity(request.getCapacity());
        room.setStars(request.getStars());
        room.setPricePerNight(request.getPricePerNight());

        return room;
    }
}
