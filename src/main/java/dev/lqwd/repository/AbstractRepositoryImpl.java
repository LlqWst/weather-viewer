package dev.lqwd.repository;

import dev.lqwd.exception.DataBaseException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class AbstractRepositoryImpl<T, K> implements AbstractRepository<T, K> {

    private static final String SELECT_ALL = "SELECT e FROM %s AS e";
    private static final String ERROR_MESSAGE_ID_NOT_FOUND = "%s not found with id: %s";

    @PersistenceContext
    protected EntityManager entityManager;

    private final Class<T> entity;
    private final String entityName;

    public AbstractRepositoryImpl(Class<T> entity) {
        this.entity = entity;
        this.entityName = entity.getSimpleName();
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        return entityManager.createQuery(SELECT_ALL.formatted(entityName), entity)
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<T> findById(K id) {
        return Optional.ofNullable(
                entityManager.find(entity, id));
    }

    @Override
    @Transactional
    public T save(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    @Transactional
    public void deleteById(K id) {
        T entity = findById(id)
                .orElseThrow(() -> new DataBaseException(ERROR_MESSAGE_ID_NOT_FOUND.formatted(entityName, id)));
        entityManager.remove(entity);
    }

    @Override
    @Transactional
    public int deleteAll() {
        return entityManager.createQuery("DELETE FROM " + entityName)
                .executeUpdate();

    }

}
