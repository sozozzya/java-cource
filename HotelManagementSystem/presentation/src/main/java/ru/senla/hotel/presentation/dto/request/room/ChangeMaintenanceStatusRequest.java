package ru.senla.hotel.presentation.dto.request.room;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangeMaintenanceStatusRequest {

    private boolean underMaintenance;
}
