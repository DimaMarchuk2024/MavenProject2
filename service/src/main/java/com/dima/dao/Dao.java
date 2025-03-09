package com.dima.dao;

import com.dima.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface Dao<K extends Serializable, E extends BaseEntity<K>> {

    E save(E entity);

    Optional<E> getById(K id);

    void update(E entity);

    List<E> findAll();

    void delete(E entity);
}
