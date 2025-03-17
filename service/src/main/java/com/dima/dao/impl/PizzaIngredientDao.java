package com.dima.dao.impl;

import com.dima.dao.DaoBase;
import com.dima.entity.PizzaIngredient;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class PizzaIngredientDao extends DaoBase<Integer, PizzaIngredient> {

    public PizzaIngredientDao(EntityManager entityManager) {
        super(PizzaIngredient.class, entityManager);
    }
}
