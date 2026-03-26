package ru.senla.hotel.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource(ApplicationConfig config) {

        DriverManagerDataSource ds = new DriverManagerDataSource();

        ds.setDriverClassName(config.getJdbcDriver());
        ds.setUrl(config.getJdbcUrl());
        ds.setUsername(config.getJdbcUser());
        ds.setPassword(config.getJdbcPassword());

        return ds;
    }
}
