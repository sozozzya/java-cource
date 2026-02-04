package ru.senla.hotel.db;

import ru.senla.hotel.config.ApplicationConfig;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.exception.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public final class ConnectionManager {

    @Inject
    private ApplicationConfig config;

    private Connection connection;

    public ConnectionManager() {
    }

    public synchronized Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName(config.getJdbcDriver());

                connection = DriverManager.getConnection(
                        config.getJdbcUrl(),
                        config.getJdbcUser(),
                        config.getJdbcPassword()
                );

                connection.setAutoCommit(false);

            } catch (Exception e) {
                throw new DatabaseException("Failed to initialize DB connection", e);
            }
        }
        return connection;
    }

    public void commit() {
        try {
            getConnection().commit();
        } catch (SQLException e) {
            throw new DatabaseException("Commit failed", e);
        }
    }

    public void rollback() {
        try {
            getConnection().rollback();
        } catch (SQLException e) {
            throw new DatabaseException("Rollback failed", e);
        }
    }
}
