package com.dima.dao.impl;

import com.dima.dao.DaoBase;
import com.dima.entity.Order;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao extends DaoBase<Long, Order> {

    public OrderDao(EntityManager entityManager) {
        super(Order.class, entityManager);
    }
}
