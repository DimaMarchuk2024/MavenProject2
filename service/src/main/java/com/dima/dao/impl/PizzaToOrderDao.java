package com.dima.dao.impl;

import com.dima.dao.DaoBase;
import com.dima.entity.PizzaToOrder;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class PizzaToOrderDao extends DaoBase<Long, PizzaToOrder> {

    public PizzaToOrderDao(Session session) {
        super(PizzaToOrder.class, session);
    }
}
