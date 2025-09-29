package dev.lqwd.configuration.persistence;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;

@Slf4j
@Configuration
@Profile({"dev", "prod"})
@PropertySource("classpath:app.properties")
public class FlywayConfig {

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

}