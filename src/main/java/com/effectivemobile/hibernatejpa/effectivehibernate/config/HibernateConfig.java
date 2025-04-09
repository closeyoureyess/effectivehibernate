package com.effectivemobile.hibernatejpa.effectivehibernate.config;

import com.effectivemobile.hibernatejpa.effectivehibernate.entities.Task;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class HibernateConfig {

    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String userName;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.jpa.database-platform}")
    private String databasePlatform;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String hibernateDdl;

    @Bean
    public SessionFactory sessionFactory() {
        Configuration configuration = new Configuration();

        configuration.setProperty("hibernate.connection.driver_class", driverClassName);
        configuration.setProperty("hibernate.connection.url", url);
        configuration.setProperty("hibernate.connection.username", userName);
        configuration.setProperty("hibernate.connection.password", password);
        configuration.setProperty("hibernate.dialect", databasePlatform);
        configuration.setProperty("hibernate.hbm2ddl.auto", hibernateDdl);

        configuration.addAnnotatedClass(Task.class);

        return configuration.buildSessionFactory();
    }

    @Bean
    @Primary // чтобы этот бин использовался по умолчанию
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .url(url)
                .username(userName)
                .password(password)
                .driverClassName(driverClassName)
                .build();
    }
}
