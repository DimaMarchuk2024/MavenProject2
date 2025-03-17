package com.dima.dao.impl;

import com.dima.dao.DaoBase;
import com.dima.entity.DeliveryAddress;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class DeliveryAddressDao extends DaoBase<Long, DeliveryAddress> {

    public DeliveryAddressDao(EntityManager entityManager) {
        super(DeliveryAddress.class, entityManager);
    }
}
