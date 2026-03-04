package ru.senla.hotel.config;

import ru.senla.hotel.autoconfig.annotation.ConfigProperty;
import ru.senla.hotel.di.annotation.Component;

@Component
public class ApplicationConfig {

    @ConfigProperty(propertyName = "jdbc.url")
    private String jdbcUrl;

    @ConfigProperty(propertyName = "jdbc.user")
    private String jdbcUser;

    @ConfigProperty(propertyName = "jdbc.password")
    private String jdbcPassword;

    @ConfigProperty(propertyName = "jdbc.driver")
    private String jdbcDriver;

    @ConfigProperty(propertyName = "room.status.change.enabled")
    private boolean roomStatusChangeEnabled;

    @ConfigProperty(propertyName = "room.history.size")
    private int roomHistorySize;

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public String getJdbcUser() {
        return jdbcUser;
    }

    public String getJdbcPassword() {
        return jdbcPassword;
    }

    public String getJdbcDriver() {
        return jdbcDriver;
    }

    public boolean isRoomStatusChangeEnabled() {
        return roomStatusChangeEnabled;
    }

    public int getRoomHistorySize() {
        return roomHistorySize;
    }
}
