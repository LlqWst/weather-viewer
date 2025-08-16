package dev.lqwd;

import dev.lqwd.configuration.PersistenceConfig;
import dev.lqwd.configuration.WebMvcConfig;
import lombok.NonNull;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { PersistenceConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { WebMvcConfig.class };
    }

    @Override
    protected String @NonNull [] getServletMappings() {
        return new String[] { "/" };
    }
}