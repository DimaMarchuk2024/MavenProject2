package com.dima.dao.impl;

import com.dima.dao.DaoBase;
import com.dima.entity.Order;
import org.hibernate.Session;

public class OrderDao extends DaoBase<Long, Order> {
    public OrderDao(Session session) {
        super(Order.class, session);
    }
}
