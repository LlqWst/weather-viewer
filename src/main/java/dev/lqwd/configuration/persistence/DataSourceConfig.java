package dev.lqwd.configuration.persistence;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;


@Slf4j
@Configuration
@Profile({"dev", "prod"})
@PropertySource("classpath:app.properties")
public class DataSourceConfig {

    public static final String CREDENTIALS_NOT_SET = "Database credentials not set";

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
            log.error(CREDENTIALS_NOT_SET);
            throw new IllegalStateException(CREDENTIALS_NOT_SET);
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


}