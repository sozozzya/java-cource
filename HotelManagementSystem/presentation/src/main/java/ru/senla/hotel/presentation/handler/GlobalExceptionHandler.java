package ru.senla.hotel.presentation.handler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.senla.hotel.application.exception.BusinessException;
import ru.senla.hotel.application.exception.booking.RoomUnavailableException;
import ru.senla.hotel.presentation.exception.ApiError;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(
            EntityNotFoundException ex,
            HttpServletRequest request) {

        ApiError error = new ApiError(
                404,
                "NOT_FOUND",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(RoomUnavailableException.class)
    public ResponseEntity<ApiError> handleRoomUnavailable(
            RoomUnavailableException ex,
            HttpServletRequest request) {

        ApiError error = new ApiError(
                409,
                "ROOM_UNAVAILABLE",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusiness(
            BusinessException ex,
            HttpServletRequest request) {

        ApiError error = new ApiError(
                400,
                "BUSINESS_ERROR",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(
            Exception ex,
            HttpServletRequest request) {

        ApiError error = new ApiError(
                500,
                "INTERNAL_ERROR",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}