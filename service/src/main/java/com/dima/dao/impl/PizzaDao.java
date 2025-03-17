package com.dima.dao.impl;

import com.dima.dao.DaoBase;
import com.dima.entity.Pizza;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class PizzaDao extends DaoBase<Integer, Pizza> {

    public PizzaDao(EntityManager entityManager) {
        super(Pizza.class, entityManager);
    }
}
