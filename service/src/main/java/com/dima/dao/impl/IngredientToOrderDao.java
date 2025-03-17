package com.dima.dao.impl;

import com.dima.dao.DaoBase;
import com.dima.entity.IngredientToOrder;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class IngredientToOrderDao extends DaoBase<Long, IngredientToOrder> {

    public IngredientToOrderDao(EntityManager entityManager) {
        super(IngredientToOrder.class, entityManager);
    }
}
