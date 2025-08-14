package dev.lqwd;

import dev.lqwd.exception.DataBaseException;
import dev.lqwd.utils.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import lombok.extern.slf4j.Slf4j;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@jakarta.servlet.annotation.WebListener
public class WebListener implements ServletContextListener {

    private static final String PATH_TO_INIT_DATA = "/sql_scripts/init_data.sql";
    private static final String INIT_SCRIPT_ERROR_MESSAGE = "DB Error: file with init data wasn't found";
    private static final String DB_ERROR_MESSAGE = "DB Error on loadInitData";

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        HibernateUtil.init();
        loadInitData();

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        HibernateUtil.close();

    }

    private static void loadInitData() {
        try (InputStream inputStream = HibernateUtil.class.getResourceAsStream(PATH_TO_INIT_DATA)) {
            if (inputStream == null) {
                throw new DataBaseException(INIT_SCRIPT_ERROR_MESSAGE);
            }

            try (EntityManager entityManager = HibernateUtil.getEntityManager()) {

                String sql = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                entityManager.getTransaction().begin();

                try {

                    entityManager.createNativeQuery(sql).executeUpdate();
                    entityManager.getTransaction().commit();

                } catch (Exception e) {

                    if (entityManager.getTransaction().isActive()) {
                        entityManager.getTransaction().rollback();
                    }

                    throw e;
                }
            }

        } catch (Exception e) {

            log.error("Initial load error", e);
            throw new DataBaseException(DB_ERROR_MESSAGE);

        }
    }

}
