package dev.lqwd.utils;

import dev.lqwd.exception.DataBaseException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

@UtilityClass
@Slf4j
public final class HibernateUtil {

    private static final String PASSWORD = Objects.requireNonNull(
            System.getenv("DB_PASS"), "DB_PASS environment variable is not set");

    private static final String USER = Objects.requireNonNull(
            System.getenv("DB_USER"), "DB_USER environment variable is not set");

    private static final ReentrantLock lock = new ReentrantLock();
    private static volatile EntityManagerFactory emf;

    public static void init() {
        if (emf == null || !emf.isOpen()) {
            lock.lock();
            try {
                if (emf == null || !emf.isOpen()) {

                    emf = Persistence.createEntityManagerFactory(
                            "persistence-postgreSQL",
                            getHibernateParameters());

                    log.info("EntityManagerFactory initialized successfully");
                }
            } catch (Exception e) {
                log.error("Database initialization failed", e);
                throw new DataBaseException("Failed to initialize database");
            } finally {
                lock.unlock();
            }
        }
    }

    private static Map<String, String> getHibernateParameters() {
        Map<String, String> parameters = new HashMap<>();

        parameters.put("hibernate.hikari.username", USER);
        parameters.put("hibernate.hikari.password", PASSWORD);

        return parameters;

    }

    public static EntityManager getEntityManager() throws RuntimeException {
        if (emf == null || !emf.isOpen()) {
            throw new DataBaseException("EntityManagerFactory is not initialized");
        }
        return emf.createEntityManager();
    }

    public static void close() {

        if (emf != null && emf.isOpen()) {
            emf.close();
            log.info("EntityManagerFactory closed successfully");
        }

    }

}
