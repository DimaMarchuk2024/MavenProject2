package com.dima.dao.impl;

import com.dima.dao.DaoBase;
import com.dima.entity.Ingredient;
import org.hibernate.Session;

public class IngredientDao extends DaoBase<Integer, Ingredient> {

    public IngredientDao(Session session) {
        super(Ingredient.class, session);
    }
}
