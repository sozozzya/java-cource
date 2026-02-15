package ru.senla.hotel.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import ru.senla.hotel.ApplicationConfig;
import ru.senla.hotel.autoconfig.ConfigLoader;

import java.util.HashMap;
import java.util.Map;

public class JpaUtil {

    private static final EntityManagerFactory emf;

    static {

        ApplicationConfig config = ConfigLoader.load(ApplicationConfig.class);

        Map<String, Object> properties = new HashMap<>();

        properties.put("jakarta.persistence.jdbc.url", config.getJdbcUrl());
        properties.put("jakarta.persistence.jdbc.user", config.getJdbcUser());
        properties.put("jakarta.persistence.jdbc.password", config.getJdbcPassword());
        properties.put("jakarta.persistence.jdbc.driver", config.getJdbcDriver());

        emf = Persistence.createEntityManagerFactory("hotelPU", properties);
    }

    public static EntityManager createEntityManager() {
        return emf.createEntityManager();
    }
}
