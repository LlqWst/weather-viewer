package config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Slf4j
@Profile("test")
@Configuration
@EnableTransactionManagement
@ComponentScan({
        "dev.lqwd.service",
        "dev.lqwd.repository",
        "dev.lqwd.uri_api_builder",
        "dev.lqwd.controller",
        "dev.lqwd.interceptor",
        "dev.lqwd.exception_handler",
        "dev.lqwd.configuration",
        "dev.lqwd.mapper"
})
@PropertySource("classpath:app_test.properties")
public class TestPersistenceConfig {


    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
    
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:schema_h2.sql")
                .build();
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
