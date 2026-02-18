package ru.senla.hotel.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan("ru.senla.hotel")
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class SpringConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public DataSource dataSource(ApplicationConfig config) {

        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(config.getJdbcDriver());
        ds.setUrl(config.getJdbcUrl());
        ds.setUsername(config.getJdbcUser());
        ds.setPassword(config.getJdbcPassword());

        return ds;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        vendorAdapter.setGenerateDdl(false);
        vendorAdapter.setShowSql(false);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

        factory.setDataSource(dataSource);
        factory.setPackagesToScan("ru.senla.hotel.domain");
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setJpaPropertyMap(jpaProperties());

        return factory;
    }

    @Bean
    public Map<String, Object> jpaProperties() {

        Map<String, Object> properties = new HashMap<>();

        properties.put("hibernate.hbm2ddl.auto", "validate");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.format_sql", false);

        return properties;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
