package com.dima.dao.impl;

import com.dima.dao.DaoBase;
import com.dima.entity.PizzaIngredient;
import org.hibernate.Session;

public class PizzaIngredientDao extends DaoBase<Integer, PizzaIngredient> {

    public PizzaIngredientDao(Session session) {
        super(PizzaIngredient.class, session);
    }
}
