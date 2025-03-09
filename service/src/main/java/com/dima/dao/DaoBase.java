package com.dima.dao;

import com.dima.entity.BaseEntity;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class DaoBase<K extends Serializable, E extends BaseEntity<K>> implements Dao<K, E> {

    private final Class<E> clazz;
    private final Session session;
    @Override
    public E save(E entity) {
        session.persist(entity);
        return entity;
    }

    @Override
    public Optional<E> getById(K id) {
        return Optional.ofNullable(session.find(clazz, id));
    }

    @Override
    public void update(E entity) {
        session.merge(entity);
    }

    @Override
    public List<E> findAll() {
        CriteriaQuery<E> criteria = session.getCriteriaBuilder().createQuery(clazz);
        criteria.from(clazz);
        return session.createQuery(criteria).getResultList();
    }

    @Override
    public void delete(E entity) {
        session.remove(entity);
        session.flush();
    }
}
