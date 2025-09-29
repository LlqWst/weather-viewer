package dev.lqwd.configuration.persistence;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;


import javax.sql.DataSource;
import java.util.Properties;

@Slf4j
@Configuration
@Profile({"dev", "prod"})
@PropertySource("classpath:app.properties")
public class JpaConfig {

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

}
