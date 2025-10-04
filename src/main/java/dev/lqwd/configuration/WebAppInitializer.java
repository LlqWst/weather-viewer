package dev.lqwd.configuration;

import dev.lqwd.configuration.persistence.DataSourceConfig;
import dev.lqwd.configuration.persistence.FlywayConfig;
import dev.lqwd.configuration.persistence.JpaConfig;
import dev.lqwd.configuration.persistence.TransactionConfig;
import dev.lqwd.configuration.web.WebMvcConfig;
import dev.lqwd.filter.WhiteListFilter;
import jakarta.servlet.Filter;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import lombok.NonNull;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    public static final String PROFILE = "prod";

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.setInitParameter("spring.profiles.active", PROFILE);
        super.onStartup(servletContext);
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{
                DataSourceConfig.class,
                FlywayConfig.class,
                JpaConfig.class,
                TransactionConfig.class
        };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{
                WebMvcConfig.class
        };
    }

    @Override
    protected String @NonNull [] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Filter[] getServletFilters() {
        return new Filter[]{
                new WhiteListFilter()
        };
    }
}