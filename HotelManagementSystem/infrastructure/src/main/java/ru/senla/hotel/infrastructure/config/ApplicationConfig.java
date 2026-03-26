package ru.senla.hotel.infrastructure.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Getter
@Configuration
@ComponentScan("ru.senla.hotel")
@PropertySource("classpath:application.properties")
public class ApplicationConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    private final String jdbcUrl;
    private final String jdbcUser;
    private final String jdbcPassword;
    private final String jdbcDriver;
    private final boolean roomStatusChangeEnabled;
    private final int roomHistorySize;

    public ApplicationConfig(
            @Value("${jdbc.url}") String jdbcUrl,
            @Value("${jdbc.user}") String jdbcUser,
            @Value("${jdbc.password}") String jdbcPassword,
            @Value("${jdbc.driver}") String jdbcDriver,
            @Value("${room.status.change.enabled:true}") boolean roomStatusChangeEnabled,
            @Value("${room.history.size:10}") int roomHistorySize
    ) {
        this.jdbcUrl = requireNonBlank(jdbcUrl, "jdbc.url");
        this.jdbcUser = requireNonBlank(jdbcUser, "jdbc.user");
        this.jdbcPassword = requireNonBlank(jdbcPassword, "jdbc.password");
        this.jdbcDriver = requireNonBlank(jdbcDriver, "jdbc.driver");
        this.roomStatusChangeEnabled = roomStatusChangeEnabled;
        this.roomHistorySize = roomHistorySize;
    }

    private String requireNonBlank(String value, String property) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Required property '" + property + "' is missing or blank");
        }
        return value;
    }
}
