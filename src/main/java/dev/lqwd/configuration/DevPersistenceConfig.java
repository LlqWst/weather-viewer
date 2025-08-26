package dev.lqwd.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Slf4j
@Profile("dev")
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("dev.lqwd.repository")
@ComponentScan("dev.lqwd.service")
@PropertySource("classpath:app.properties")
@EnableScheduling
public class DevPersistenceConfig {

    @Bean
    public DataSource dataSource(
            @Value("${DB_USER:}") String user,
            @Value("${DB_PASS:}") String password,
            @Value("${db.url}") String url,
            @Value("${db.driver}") String driver,
            @Value("${hibernate.hikari.minimumIdle}") int minimumIdle,
            @Value("${hibernate.hikari.maximumPoolSize}") int maximumPoolSize,
            @Value("${hibernate.hikari.idleTimeout}") int idleTimeout,
            @Value("${hibernate.hikari.connectionTimeout}") int connectionTimeout,
            @Value("${hibernate.hikari.maxLifetime}") int maxLifetime) {

        if (user.isEmpty() || password.isEmpty()) {
            log.error("Database credentials not set");
            throw new IllegalStateException("Database credentials not set");
        }

        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(user);
        hikariConfig.setPassword(password);
        hikariConfig.setDriverClassName(driver);

        hikariConfig.setMinimumIdle(minimumIdle);
        hikariConfig.setMaximumPoolSize(maximumPoolSize);
        hikariConfig.setIdleTimeout(idleTimeout);
        hikariConfig.setConnectionTimeout(connectionTimeout);
        hikariConfig.setMaxLifetime(maxLifetime);

        log.info("HikariCP Config: {}", hikariConfig);

        return new HikariDataSource(hikariConfig);
    }

    @Bean(initMethod = "migrate")
    public Flyway flyway(
            DataSource dataSource,
            @Value("${flyway.locations}") String[] locations,
            @Value("${flyway.validateOnMigrate}") boolean isValidateOnMigrate,
            @Value("${flyway.schema}") String schema) {

        return Flyway.configure()
                .dataSource(dataSource)
                .locations(locations)
                .validateOnMigrate(isValidateOnMigrate)
                .schemas(schema)
                .load();

    }

    @Bean
    @DependsOn("flyway")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource,
            @Qualifier("properties") Properties hibernateProperties) {

        LocalContainerEntityManagerFactoryBean containerEntityManagerFactory = new LocalContainerEntityManagerFactoryBean();

        containerEntityManagerFactory.setDataSource(dataSource);
        containerEntityManagerFactory.setPackagesToScan("dev.lqwd.entity");
        containerEntityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        containerEntityManagerFactory.setJpaProperties(hibernateProperties);

        return containerEntityManagerFactory;
    }

    @Bean
    public Properties properties(
            @Value("${hibernate.show_sql}") boolean isShowSql,
            @Value("${hibernate.format_sql}") boolean isFormatSql,
            @Value("${hibernate.hbm2ddl.auto}") String hbm2ddl) {

        Properties properties = new Properties();

        properties.put("hibernate.show_sql", isShowSql);
        properties.put("hibernate.format_sql", isFormatSql);
        properties.put("hibernate.hbm2ddl.auto", hbm2ddl);

        return properties;
    }

    @Bean
    public PlatformTransactionManager transactionManager(
            LocalContainerEntityManagerFactoryBean containerEntityManagerFactory) {

        JpaTransactionManager transactionManager = new JpaTransactionManager();

        if (containerEntityManagerFactory.getObject() == null) {
            log.error("EntityManagerFactory is not initialized, {}", containerEntityManagerFactory);
            throw new IllegalStateException("EntityManagerFactory is not initialized");
        }

        transactionManager.setEntityManagerFactory(containerEntityManagerFactory.getObject());

        return transactionManager;

    }

}