package dev.lqwd.repository;

import java.util.List;
import java.util.Optional;

public interface AbstractRepository<T, K> {

    List<T> findAll();

    Optional<T> findById(K id);

    T save(T entity);

    void deleteById(K id);

    int deleteAll();
}
