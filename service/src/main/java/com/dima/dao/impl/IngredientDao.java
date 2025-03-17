package com.dima.dao.impl;

import com.dima.dao.DaoBase;
import com.dima.entity.Ingredient;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class IngredientDao extends DaoBase<Integer, Ingredient> {

    public IngredientDao(EntityManager entityManager) {
        super(Ingredient.class, entityManager);
    }
}
