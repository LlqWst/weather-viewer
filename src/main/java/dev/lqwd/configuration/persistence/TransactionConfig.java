package dev.lqwd.configuration.persistence;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.*;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Slf4j
@Configuration
@Profile({"dev", "prod"})
@EnableTransactionManagement
@EnableScheduling
public class TransactionConfig {

    public static final String FACTORY_IS_NOT_INITIALIZED = "EntityManagerFactory is not initialized";

    @Bean
    public PlatformTransactionManager transactionManager(
            LocalContainerEntityManagerFactoryBean containerEntityManagerFactory) {

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        if (containerEntityManagerFactory.getObject() == null) {
            log.error("EntityManagerFactory is not initialized, {}", containerEntityManagerFactory);
            throw new IllegalStateException(FACTORY_IS_NOT_INITIALIZED);
        }
        transactionManager.setEntityManagerFactory(containerEntityManagerFactory.getObject());
        return transactionManager;

    }

}