package ru.senla.hotel.presentation.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import ru.senla.hotel.infrastructure.config.FlywayConfig;
import ru.senla.hotel.infrastructure.config.TransactionConfig;
import ru.senla.hotel.infrastructure.config.JpaConfig;
import ru.senla.hotel.infrastructure.config.ApplicationConfig;
import ru.senla.hotel.infrastructure.config.DataSourceConfig;
import ru.senla.hotel.presentation.security.config.SecurityConfig;

public class WebAppInitializer
        extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {

        return new Class[]{
                ApplicationConfig.class,
                DataSourceConfig.class,
                JpaConfig.class,
                TransactionConfig.class,
                FlywayConfig.class,
                SecurityConfig.class
        };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {

        return new Class[]{
                WebMvcConfig.class,
                JacksonConfig.class
        };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
