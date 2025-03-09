package com.dima.dao.impl;

import com.dima.dao.DaoBase;
import com.dima.entity.IngredientToOrder;
import org.hibernate.Session;

public class IngredientToOrderDao extends DaoBase<Long, IngredientToOrder> {

    public IngredientToOrderDao(Session session) {
        super(IngredientToOrder.class, session);
    }
}
