package com.dima.dao.impl;

import com.dima.dao.DaoBase;
import com.dima.entity.Pizza;
import org.hibernate.Session;

public class PizzaDao extends DaoBase<Integer, Pizza> {

    public PizzaDao(Session session) {
        super(Pizza.class, session);
    }
}
