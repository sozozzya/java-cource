package ru.senla.hotel.presentation.dto.response.guest;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GuestPaymentResponse {

    private String guestName;
    private BigDecimal totalAmount;
}
