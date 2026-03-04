CREATE TABLE guests (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE rooms (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    number INT NOT NULL,
    capacity INT NOT NULL,
    stars INT NOT NULL,
    price_per_night NUMERIC(10,2) NOT NULL,
    is_under_maintenance BOOLEAN NOT NULL
);

CREATE TABLE services (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    price NUMERIC(10,2) NOT NULL
);

CREATE TABLE bookings (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    guest_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,

    CONSTRAINT fk_booking_guest
        FOREIGN KEY (guest_id) REFERENCES guests(id),

    CONSTRAINT fk_booking_room
        FOREIGN KEY (room_id) REFERENCES rooms(id)
);

CREATE TABLE booking_services (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    booking_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    date DATE NOT NULL,

    CONSTRAINT fk_bs_booking
        FOREIGN KEY (booking_id) REFERENCES bookings(id),

    CONSTRAINT fk_bs_service
        FOREIGN KEY (service_id) REFERENCES services(id)
);
